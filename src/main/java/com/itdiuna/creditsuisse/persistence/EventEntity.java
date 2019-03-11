package com.itdiuna.creditsuisse.persistence;

public class EventEntity {

	private final String id;
	private final long duration;
	private final String type;
	private final String host;
	private final boolean alert;

	public EventEntity(String id, long duration, String type, String host) {
		this.id = id;
		this.duration = duration;
		this.type = type;
		this.host = host;
		alert = duration > 4L;
	}

	public String getId() {
		return id;
	}

	public long getDuration() {
		return duration;
	}

	public String getType() {
		return type;
	}

	public String getHost() {
		return host;
	}

	public boolean getAlert() {
		return alert;
	}
}
