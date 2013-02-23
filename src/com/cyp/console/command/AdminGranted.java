package com.cyp.console.command;

import java.io.Serializable;

public class AdminGranted implements Serializable {
		
	private static final long serialVersionUID = 1080788506836489318L;
	
	private String participant;

	public AdminGranted(String participant) {
		this.participant = participant;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}
}
