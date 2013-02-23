package com.cyp.console;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.cyp.application.Application;

public class ServerCommandHandler extends SimpleChannelUpstreamHandler {

	static final ChannelGroup channels = new DefaultChannelGroup();
	
	private List<ServerCommandListener> listeners;
	
	public ServerCommandHandler(){
		this.listeners = new ArrayList<>();
	}
	
	public void addServerCommandListener(ServerCommandListener listener){
		if( !this.listeners.contains(listener)){
			this.listeners.add(listener);
		}
	}
	
	public void removeServerCommandListener(ServerCommandListener listener){
		if( this.listeners.contains(listener)){
			this.listeners.remove(listener);
		}
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		Application.getContext().getLogger().debug("ServerCommandHandler::channelConnected", e.getChannel().getRemoteAddress().toString());
		channels.add(ctx.getChannel());
		
		for( ServerCommandListener listener : this.listeners ){
			listener.onClientConnected(ctx.getChannel().getRemoteAddress().toString());			
		}
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent
				&& ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
			Application.getContext().getLogger().debug("ServerCommandHandler::handleUpstream", e.toString());			
		}

		super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Application.getContext().getLogger().debug("ServerCommandHandler::messageReceived", e.toString());
		
		for( ServerCommandListener listener : this.listeners ){
			listener.onClientCommand(e.getMessage());			
		}
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		Application.getContext().getLogger().debug("ServerCommandHandler::channelDisconnected", e.getChannel().getRemoteAddress().toString());	
		channels.remove(e.getChannel());
		
		for( ServerCommandListener listener : this.listeners ){
			listener.onClientDisconnected(ctx.getChannel().getRemoteAddress().toString());			
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Application.getContext().getLogger().error("ServerCommandHandler::exceptionCaught", e.toString(),e.getCause());			
		e.getChannel().close();
	}
	
	public void sendObject(Object obj){
		Application.getContext().getLogger().debug("ServerCommandHandler::sendObject", obj.toString());		
		channels.write(obj);		
	}
}
