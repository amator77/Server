package com.cyp.console;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ConsoleClientHandler extends SimpleChannelUpstreamHandler {
	
	private Channel channel;
	
	private ConsoleClientHanlderListener messageReceiveHandler;
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {		
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {				
		channel = ctx.getChannel();
		this.messageReceiveHandler.onMessage("Connected to :"+ctx.getChannel().getRemoteAddress().toString());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {						
		if( this.messageReceiveHandler != null ){
			this.messageReceiveHandler.onMessage(e.getMessage());
		}
	}
	
	public void sendMessage(Object message){
		if( this.channel != null && this.channel.isConnected() ){
			this.channel.write(message);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();		
		e.getChannel().close();
	}

	public ConsoleClientHanlderListener getMessageReceiveHandler() {
		return messageReceiveHandler;
	}

	public void setMessageReceiveHandler(
			ConsoleClientHanlderListener messageReceiveHandler) {
		this.messageReceiveHandler = messageReceiveHandler;
	}
}
