package com.cyp.console.command;

import java.io.Serializable;

public class ParticipantBanned implements Serializable {
	
	private static final long serialVersionUID = -8508336558097144889L;

	private String participant;
	
	private String actor;
	
	private String reason;
	
	public ParticipantBanned(String participant,String actor,String reason){
		this.participant = participant;
		this.actor = actor;
		this.reason = reason;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	@Override
	public String toString() {
		return "ParticipantBanned [participant=" + participant + ", actor="
				+ actor + ", reason=" + reason + "]";
	}	
}
