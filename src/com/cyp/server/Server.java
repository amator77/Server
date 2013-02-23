package com.cyp.server;

import java.io.IOException;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import com.cyp.application.Application;
import com.cyp.application.Logger;
import com.cyp.console.ConsoleServer;
import com.cyp.console.ServerCommandListener;
import com.cyp.console.command.AdminGranted;
import com.cyp.console.command.AdminRevoked;
import com.cyp.console.command.MembershipGranted;
import com.cyp.console.command.MembershipRevoked;
import com.cyp.console.command.ModeratorGranted;
import com.cyp.console.command.ModeratorRevoked;
import com.cyp.console.command.NicknameChanged;
import com.cyp.console.command.OwnershipGranted;
import com.cyp.console.command.OwnershipRevoked;
import com.cyp.console.command.ParticipantBanned;
import com.cyp.console.command.ParticipantJoined;
import com.cyp.console.command.ParticipantKicked;
import com.cyp.console.command.ParticipantLeft;
import com.cyp.console.command.RoomMessageCommand;
import com.cyp.console.command.StartCommand;
import com.cyp.console.command.StopCommand;
import com.cyp.console.command.VoiceGranted;
import com.cyp.console.command.VoiceRevoked;
import com.cyp.server.rooms.RoomsManager;
import com.cyp.server.rooms.ServerRoom;
import com.cyp.transport.Connection;
import com.cyp.transport.ConnectionFactory;
import com.cyp.transport.ConnectionFactory.CONNECTION_TYPE;
import com.cyp.transport.ConnectionListener;
import com.cyp.transport.Message;
import com.cyp.transport.xmpp.XMPPGenericConnection;

public class Server implements ConnectionListener, ParticipantStatusListener,
		PacketListener, ServerCommandListener {

	private static final String MAIN_ROOM = "cypmainroom";

	private static final String MASTER_USERNAME = "cyp.rooms.master@gmail.com";

	private static final String MASTER_PASSWORD = "leo@1977";

	private static final String MASTER_NICKNAME = "cyp_master";

	private static final int CONSOLE_PORT = 3456;

	private static final String CHESSYOUP_URL = "http://api.chessyoup.com";

	private static final String API_SECRET = "3ng2GDWbloODjOxs4d1r_Jti";

	private Connection connection;

	private ConsoleServer consoleServer;

	private MultiUserChat mainRoom;

	private Logger log;

	public Server() throws Exception {
		Application.configure("com.cyp.server.context.ServerContext", null);
		connection = ConnectionFactory.getFactory().createConnection(
				CONNECTION_TYPE.XMPP_GTALK_MD5);
		this.log = Application.getContext().getLogger();
		consoleServer = new ConsoleServer();
		consoleServer.run();
		consoleServer.addServerCommandListener(this);
	}
	
	@Override
	public void onClientCommand(Object command) {
		if (command instanceof StartCommand) {
			try {
				this.start();
			} catch (Exception e) {
				consoleServer.sendObject(e);
				e.printStackTrace();
			}
		} else if (command instanceof StopCommand) {
			try {
				this.stop();
			} catch (Exception e) {
				consoleServer.sendObject(e);
				e.printStackTrace();
			}
		}
		else if (command instanceof RoomMessageCommand) {
			RoomMessageCommand msg = (RoomMessageCommand)command;
			
			if( this.connection.isConnected() && mainRoom != null && mainRoom.isJoined() ){
				try {
					mainRoom.sendMessage(msg.getMessage());
				} catch (XMPPException e) {
					consoleServer.sendObject(e);
					e.printStackTrace();
				}
			}			
		}
	}
	
	@Override
	public void onClientConnected(String ip) {
		System.out.println("New console client from :"+ip);
		consoleServer.sendObject("Greetings from room :"+MAIN_ROOM+", Room started : "+( this.mainRoom != null && this.mainRoom.isJoined()));		
	}

	@Override
	public void onClientDisconnected(String ip) {		
		System.out.println("Console client disconected from :"+ip);
	}
	
	public void start() throws Exception {
		consoleServer.sendObject("Room" + MAIN_ROOM + " is starting.");

		if (!connection.isConnected()) {
			connection.login(MASTER_USERNAME, MASTER_PASSWORD);
			consoleServer.sendObject("Connected to server as "
					+ connection.getAccountId());
			XMPPGenericConnection xmppConn = (XMPPGenericConnection) this.connection;
			mainRoom = new MultiUserChat(xmppConn.getXmppConnection(),
					MAIN_ROOM + "@conference.jabber.org");
			DiscussionHistory dh = new DiscussionHistory();
			dh.setMaxStanzas(10);
			mainRoom.join(MASTER_NICKNAME, "cyppassword", dh, 10000);
			consoleServer.sendObject("Joine success.");
			mainRoom.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			mainRoom.addMessageListener(this);
			mainRoom.addParticipantStatusListener(this);
			xmppConn.getXmppConnection().addPacketListener(
					new PachetDebugListener(),
					new PacketTypeFilter(Packet.class));
		}
	}

	public void stop() throws Exception {
		if (connection.isConnected()) {
			consoleServer.sendObject("Room" + MAIN_ROOM + " is stoping");
			connection.logout();
			consoleServer.sendObject("Room" + MAIN_ROOM + " is closed.");
		}
	}

	/**
	 * Process messages on the he room
	 */
	@Override
	public void processPacket(Packet packet) {
		log.debug(this.getClass().getName(),
				"New room packet :" + packet.toXML());

		if (packet instanceof org.jivesoftware.smack.packet.Message) {
			this.consoleServer
					.sendObject(new RoomMessageCommand(packet.getFrom(),
							((org.jivesoftware.smack.packet.Message) packet)
									.getBody()));
		}
	}

	@Override
	public boolean messageReceived(Connection source, Message message) {
		String commandId = message
				.getHeader(IServerCommand.SERVER_COMMAND_HEADER_KEY);

		if (commandId != null) {

			log.debug(this.getClass().getName(),
					"New server command recevied :" + commandId);

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

	@Override
	public void voiceRevoked(String participant) {
		System.out.println("voiceRevoked:" + participant);
		this.consoleServer.sendObject(new VoiceRevoked(participant));
	}

	@Override
	public void voiceGranted(String participant) {
		System.out.println("voiceGranted:" + participant);
		this.consoleServer.sendObject(new VoiceGranted(participant));
	}

	@Override
	public void ownershipRevoked(String participant) {
		System.out.println("ownershipRevoked:" + participant);
		this.consoleServer.sendObject(new OwnershipRevoked(participant));
	}

	@Override
	public void ownershipGranted(String participant) {
		System.out.println("ownershipGranted:" + participant);
		this.consoleServer.sendObject(new OwnershipGranted(participant));
	}

	@Override
	public void nicknameChanged(String participant, String newNickname) {
		System.out
				.println("nicknameChanged:" + participant + "," + newNickname);
		this.consoleServer.sendObject(new NicknameChanged(participant,
				newNickname));
	}

	@Override
	public void moderatorRevoked(String participant) {
		System.out.println("moderatorRevoked:" + participant);
		this.consoleServer.sendObject(new ModeratorRevoked(participant));
	}

	@Override
	public void moderatorGranted(String participant) {
		System.out.println("moderatorGranted:" + participant);
		this.consoleServer.sendObject(new ModeratorGranted(participant));
	}

	@Override
	public void membershipRevoked(String participant) {
		System.out.println("membershipRevoked:" + participant);
		this.consoleServer.sendObject(new MembershipRevoked(participant));
	}

	@Override
	public void membershipGranted(String participant) {
		System.out.println("membershipGranted:" + participant);
		this.consoleServer.sendObject(new MembershipGranted(participant));
	}

	@Override
	public void left(String participant) {
		System.out.println("left:" + participant);
		this.consoleServer.sendObject(new ParticipantLeft(participant));
	}

	@Override
	public void kicked(String participant, String actor, String reason) {
		System.out.println("kicked:" + actor + ", reason :" + reason);
		this.consoleServer.sendObject(new ParticipantKicked(participant));
	}

	@Override
	public void joined(String participant) {
		System.out.println("joined:" + participant);
		this.consoleServer.sendObject(new ParticipantJoined(participant));
	}

	@Override
	public void banned(String participant, String actor, String reason) {
		System.out.println("banned:" + participant);
		this.consoleServer.sendObject(new ParticipantBanned(participant, actor,
				reason));
	}

	@Override
	public void adminRevoked(String participant) {
		System.out.println("adminRevoked:" + participant);
		this.consoleServer.sendObject(new AdminRevoked(participant));
	}

	@Override
	public void adminGranted(String participant) {
		System.out.println("adminGranted:" + participant);
		this.consoleServer.sendObject(new AdminGranted(participant));
	}

	private void handlePongCommand(String from) {

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

	private class PachetDebugListener implements PacketListener {
		@Override
		public void processPacket(Packet packet) {
			consoleServer.sendObject(packet.toXML());
		}
	}

	public static void main(String[] args) throws Exception {
		new Server();		
	}
}
