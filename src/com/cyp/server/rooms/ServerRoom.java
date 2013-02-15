package com.cyp.server.rooms;

import java.util.ArrayList;
import java.util.List;

public class ServerRoom {
	
	private List<String> jids;
	
	private String name;
	
	private int capacity;
	
	public ServerRoom(String name ,int capacity){
		this.name = name;
		this.capacity = capacity;
		this.jids = new ArrayList<String>(capacity);
	}

	public List<String> getJids() {
		return jids;
	}

	public boolean addJid(String jid){
		if( !this.jids.contains(jid) ){
			this.jids.add(jid);
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean removeJid(String jid){
		if( this.jids.contains(jid) ){
			this.jids.remove(jid);
			return true;
		}
		else{
			return false;
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isFull() {		
		return this.jids.size() >= this.capacity;
	}		
}
