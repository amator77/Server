package com.cyp.console.command;

import java.io.Serializable;

public class Command implements Serializable {

	/**
	 * serial ID
	 */
	private static final long serialVersionUID = -4050613129729300108L;
		
	public static final int UNKNOWN_COMMAND = 0;
	
	public static final int LOGIN_COMMAND = 1;
	
	private int id;
	
	private byte[] payload;
	
	public Command(){
		this.id = UNKNOWN_COMMAND;
		this.payload = new byte[0];
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}		
}
