package com.cyp.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.cyp.application.Application;
import com.cyp.application.Logger;
import com.cyp.server.rooms.RoomsManager;
import com.cyp.server.rooms.ServerRoom;
import com.cyp.transport.Connection;
import com.cyp.transport.ConnectionFactory;
import com.cyp.transport.ConnectionFactory.CONNECTION_TYPE;
import com.cyp.transport.ConnectionListener;
import com.cyp.transport.Message;
import com.cyp.transport.xmpp.XMPPGenericConnection;

public class Server implements ConnectionListener {

	private static final String MAIN_ROOM = "chessyoupmainroom";
	
	private static final String MASTER_USERNAME = "cyp.rooms.master@gmail.com";
	
	private static final String MASTER_PASSWORD = "leo@1977";
	
	private static final String MASTER_NICKNAME = "cyp_master";
	
	private static final String CHESSYOUP_URL = "http://api.chessyoup.com";
	
	private static final String API_SECRET = "3ng2GDWbloODjOxs4d1r_Jti";
	
	private Connection connection;
	
	private MultiUserChat mainRoom;
	
	private Logger log;

	public Server() throws Exception {
		Application.configure("com.cyp.server.context.ServerContext", null);
		connection = ConnectionFactory.getFactory().createConnection(
				CONNECTION_TYPE.XMPP_GTALK_MD5);
		this.log = Application.getContext().getLogger();
	}

	public void start() throws Exception {
		if (!connection.isConnected()) {
			connection.login(MASTER_USERNAME, MASTER_PASSWORD);					
			XMPPGenericConnection xmppConn = (XMPPGenericConnection)this.connection;			
			mainRoom = new MultiUserChat(xmppConn.getXmppConnection(), MAIN_ROOM+"@conference.jabber.org" );			
			mainRoom.join(MASTER_NICKNAME);
			mainRoom.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));									
			
			mainRoom.addMessageListener(new PacketListener() {
				
				@Override
				public void processPacket(Packet arg0) {
					System.out.println("new message in the room");
					
				}
			});
			
			mainRoom.addParticipantListener(new PacketListener() {
				
				@Override
				public void processPacket(Packet arg0) {
					System.out.println("participant event");
					
				}
			});									
		}
	}

	public void stop() throws Exception {
		if (connection.isConnected()) {			
			this.mainRoom.destroy("stop", "");			
			connection.logout();
		}
	}

	@Override
	public boolean messageReceived(Connection source, Message message) {
		String commandId = message
				.getHeader(IServerCommand.SERVER_COMMAND_HEADER_KEY);

		if (commandId != null) {

			log.debug(this.getClass().getName(), "New server command recevied :"
					+ commandId);

			switch (Integer.parseInt(commandId)) {
			case IServerCommand.LIST_ROOMS_COMMAND_ID:
				handleListRoomsCommand(message.getFrom());
				return true;		
			case IServerCommand.PONG_ROOM_CONTACT_COMMAND_ID:
				handlePongCommand(message.getFrom());
				return true;
			default:
				log.debug(this.getClass().getName(), "Unknonw command:"
						+ message.toString());
				return false;
			}
		} else {
			log.debug(this.getClass().getName(), "Message discarded :"
					+ message.toString());
		}

		return false;
	}

	@Override
	public void onDisconect(Connection source) {
		Application.getContext().getLogger().debug("Server", "onDisconect");
	}

	

	private void handlePongCommand(String from) {

	}

	private boolean checkSubscription(String jid) {
		return true;
	}

	private void handleListRoomsCommand(String from) {
		List<ServerRoom> rooms = RoomsManager.getManager().listRooms();
		ServerCommand scmd = new ServerCommand(
				IServerCommand.LIST_CONTACTS_ROOM_COMMAND_ID);
		StringBuffer body = new StringBuffer();

		for (ServerRoom room : rooms) {
			body.append(room.getName()).append(",");
		}

		scmd.setBody(body.toString());
		this.sendCommand(scmd, from);
	}

	private void sendCommand(ServerCommand scmd, String to) {
		scmd.setTo(to);
		scmd.setFrom(this.connection.getAccountId());

		log.debug("Server", "Send command :" + scmd.toString());

		try {
			this.connection.sendMessage(scmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
			
	private ServerRoom findRoom(String roomName) {

		for (ServerRoom room : RoomsManager.getManager().listRooms()) {
			if (room.getName().equals(roomName)) {
				return room;
			}
		}

		return null;
	}

	public static void main(String[] args) throws Exception {
		final Server server = new Server();
		server.start();		
		
		JFrame fr = new JFrame();
		JButton stopButton = new JButton("Stop");
		fr.getContentPane().add(stopButton);
		fr.pack();
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
 				try {
					server.stop();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
 				System.exit(0);
			}			
		});
		
		fr.setVisible(true);
	}
}
