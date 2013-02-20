package com.cyp.console.command;

import java.io.Serializable;

public class RoomMessageCommand implements Serializable{

	/**
	 * serial Id
	 */
	private static final long serialVersionUID = 7529578619396921541L;
	
	private String from;
	
	private String message;
	
	public RoomMessageCommand(String from,String message){		
		this.from = from;
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}		
}
