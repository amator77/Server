package com.cyp.console.command;

import java.io.Serializable;

public class ModeratorGranted implements Serializable {
	
	private static final long serialVersionUID = -6165524122732848704L;
	
	private String participant;
	
	public ModeratorGranted(String participant){
		this.participant = participant;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}		
}