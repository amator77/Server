package com.cyp.console.command;

import java.io.Serializable;

public class VoiceRevoked implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2492493336220605894L;
	private String nick;
	
	public VoiceRevoked(String nick){
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}		
}
