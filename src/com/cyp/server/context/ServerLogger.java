package com.cyp.server.context;

import java.util.Date;

import com.cyp.application.Logger;

public class ServerLogger implements Logger {
	
	public void debug(String component, String message) {
		System.out.println( new Date().toString() +" "+ component+" -> "+message);		
	}

	public void info(String component, String message) {
		System.out.println( new Date().toString() +" "+ component+" -> "+message);
	}
	
	public void error(String component, String message, Throwable ex) {
		System.out.println( new Date().toString() +" "+ component+" -> "+message);
		ex.printStackTrace();		
	}
}

