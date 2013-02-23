package com.cyp.console.command;

import java.io.Serializable;

public class ParticipantLeft implements Serializable {
			
	private static final long serialVersionUID = -861323451238309085L;
	
	private String participant;
	
	public ParticipantLeft(String participant){
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
		return "ParticipantLeft [participant=" + participant + "]";
	}		
}