package com.cyp.console;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.cyp.application.Application;

public class ConsoleServer {

	public static final int CONSOLE_PORT = 3456;

	private ServerCommandHandler serverCommandHandler;
	
	private ServerBootstrap bootstrap;
		
	public void run() {
		this.bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		serverCommandHandler = new ServerCommandHandler();

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers
								.cacheDisabled(getClass().getClassLoader())),
						serverCommandHandler);
			}
		});

		this.bootstrap.bind(new InetSocketAddress(CONSOLE_PORT));
		Application.getContext().getLogger().debug("ConsoleServer", "Console server started!");
	}
	
	public void stop(){
		this.bootstrap.shutdown();
	}
	
	public void sendObject(Object object) {
		if (this.serverCommandHandler != null) {
			this.serverCommandHandler.sendObject(object);
		}
	}

	public static void main(String[] args) throws Exception {	
		Application.configure("com.cyp.server.context.ServerContext", null);
		new ConsoleServer().run();
	}
}
