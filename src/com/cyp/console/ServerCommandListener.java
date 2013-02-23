package com.cyp.console;

public interface ServerCommandListener {
	
	public void onClientCommand(Object command);
	
	public void onClientConnected(String ip);
	
	public void onClientDisconnected(String ip);
}
