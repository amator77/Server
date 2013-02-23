package com.cyp.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.cyp.console.command.RoomMessageCommand;
import com.cyp.console.command.SendAdminMessageCommand;
import com.cyp.console.command.StartCommand;
import com.cyp.console.command.StopCommand;

public class ConsoleClient {
	public static void main(String[] args) throws Exception {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		final ConsoleClientHandler consoleClientHandler = new ConsoleClientHandler();
		
		consoleClientHandler.setMessageReceiveHandler(new ConsoleClientHanlderListener() {
			
			@Override
			public void onMessage(Object mesasge) {		
				System.out.println(mesasge.toString());				
			}
		});
		
		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers
								.cacheDisabled(getClass().getClassLoader())),
						consoleClientHandler);
			}
		});

		// Start the connection attempt.
		bootstrap.connect(new InetSocketAddress("localhost",
				ConsoleServer.CONSOLE_PORT));

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			String cmd = bf.readLine();
			
			switch (cmd) {
			case "start":
				consoleClientHandler.sendMessage(new StartCommand());
				break;
			case "stop":
				consoleClientHandler.sendMessage(new StopCommand());
				break;
			case "exit":
				System.exit(0);
				break;
			default:
				consoleClientHandler.sendMessage(new RoomMessageCommand("system", cmd));
				break;
			}
		}
	}
}
