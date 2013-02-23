package com.cyp.console.command;

import java.io.Serializable;

public class ParticipantKicked implements Serializable {
	
	private static final long serialVersionUID = 8720586398718836229L;
	
	private String participant;

	public ParticipantKicked(String participant) {
		this.participant = participant;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}
}