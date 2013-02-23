package com.cyp.console.command;

import java.io.Serializable;

public class ModeratorRevoked implements Serializable {

	private static final long serialVersionUID = 6384835628447650392L;
	
	private String participant;
	
	public ModeratorRevoked(String participant){
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
		return "ModeratorRevoked [participant=" + participant + "]";
	}	
}