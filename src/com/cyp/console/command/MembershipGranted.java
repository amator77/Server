package com.cyp.console.command;

import java.io.Serializable;

public class MembershipGranted implements Serializable {
		
	private static final long serialVersionUID = 8352151828260074934L;
	
	private String participant;

	public MembershipGranted(String participant) {
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
		return "MembershipGranted [participant=" + participant + "]";
	}
	
	
}
