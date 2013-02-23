package com.cyp.console.command;

import java.io.Serializable;

public class VoiceGranted implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2492493336220605894L;
	private String participant;
	
	public VoiceGranted(String participant){
		this.participant = participant;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}		
}