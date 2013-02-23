package com.cyp.console.command;

import java.io.Serializable;

public class AdminRevoked implements Serializable {

	private static final long serialVersionUID = 2616083985965381127L;
	
	private String participant;

	public AdminRevoked(String participant) {
		this.participant = participant;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}
}
