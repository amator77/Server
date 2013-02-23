package com.cyp.console.command;

import java.io.Serializable;

public class MembershipRevoked implements Serializable {
		
	private static final long serialVersionUID = 6008979890052686685L;
	
	private String participant;
	
	public MembershipRevoked(String participant){
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
		return "MembershipRevoked [participant=" + participant + "]";
	}	
	
	
}
