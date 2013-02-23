package com.cyp.console.command;

import java.io.Serializable;

public class NicknameChanged implements Serializable {
	
	private static final long serialVersionUID = 2516685155734671137L;

	private String participant;
	
	private String newNick;
	
	public NicknameChanged(String participant,String newNick){
		this.participant = participant;
		this.newNick = newNick;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getNewNick() {
		return newNick;
	}

	public void setNewNick(String newNick) {
		this.newNick = newNick;
	}		
}
