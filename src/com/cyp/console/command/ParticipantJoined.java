package com.cyp.console.command;

import java.io.Serializable;

public class ParticipantJoined implements Serializable {
		
	private static final long serialVersionUID = 3615897495594527495L;
	
	private String participant;

	public ParticipantJoined(String participant) {
		this.participant = participant;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}
}