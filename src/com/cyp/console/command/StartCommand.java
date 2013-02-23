package com.cyp.console.command;

import java.io.Serializable;

public class StartCommand implements Serializable {
	
	private static final long serialVersionUID = -1674101438662081470L;

	@Override
	public String toString() {
		return "StartCommand [getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
}
