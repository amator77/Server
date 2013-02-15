package com.cyp.server.rooms;

import java.util.ArrayList;
import java.util.List;

public class RoomsManager {
	private static final RoomsManager manager = new RoomsManager();
	
	List<ServerRoom> rooms;
	
	private RoomsManager(){
		this.rooms = new ArrayList<>();
		ServerRoom mainRoom = new ServerRoom("ChessYoUpMainRoom",100);
		this.rooms.add(mainRoom);
	}
	
	public static RoomsManager getManager(){
		return RoomsManager.manager;
	}
	
	public List<ServerRoom> listRooms(){
		return this.rooms;
	}
}
