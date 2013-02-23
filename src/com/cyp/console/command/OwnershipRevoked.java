package com.cyp.console.command;

import java.io.Serializable;

public class OwnershipRevoked implements Serializable {
		
	private static final long serialVersionUID = 2492493336220605894L;
	
	private String participant;
	
	public OwnershipRevoked(String participant){
		this.participant = participant;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}		
}