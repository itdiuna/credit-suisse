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

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getHost() {
		return host;
	}

	@Override
	public String toString() {
		return "Event{" +
				"startTime=" + startTime +
				", endTime=" + endTime +
				", id='" + id + '\'' +
				", type='" + type + '\'' +
				", host='" + host + '\'' +
				'}';
	}
}
