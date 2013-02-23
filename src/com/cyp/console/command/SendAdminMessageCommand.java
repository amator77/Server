package com.cyp.console.command;

import java.io.Serializable;

public class SendAdminMessageCommand implements Serializable{
	
	private static final long serialVersionUID = -7933629747511178090L;
	
	private String message;
	
	public SendAdminMessageCommand(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "SendAdminMessageCommand [message=" + message + "]";
	}	
}
