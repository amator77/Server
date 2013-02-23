package com.cyp.console.command;

import java.io.Serializable;

public class OwnershipGranted implements Serializable {

	private static final long serialVersionUID = 8585570787144554886L;
	
	private String participant;
	
	public OwnershipGranted(String participant){
		this.participant = participant;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	@Override
	public String toString() {
		return "OwnershipGranted [participant=" + participant + "]";
	}	
	
	
}
