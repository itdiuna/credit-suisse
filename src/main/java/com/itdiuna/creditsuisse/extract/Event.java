package com.itdiuna.creditsuisse.extract;

public class Event {

	final long startTime;

	final long endTime;

	final String id;

	final String type;

	final String host;

	public Event(long startTime, long endTime, String id, String type, String host) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.id = id;
		this.type = type;
		this.host = host;
	}
}
