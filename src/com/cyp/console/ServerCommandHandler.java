package com.cyp.console;

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

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		Application.getContext().getLogger().debug("ServerCommandHandler::channelConnected", e.getChannel().getRemoteAddress().toString());
		channels.add(ctx.getChannel());
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
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		Application.getContext().getLogger().debug("ServerCommandHandler::channelDisconnected", e.getChannel().getRemoteAddress().toString());	
		channels.remove(e.getChannel());
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
