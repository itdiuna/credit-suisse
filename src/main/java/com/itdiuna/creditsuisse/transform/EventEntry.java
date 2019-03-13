package com.itdiuna.creditsuisse.transform;

import java.util.Objects;

public class EventEntry {

	final String id;

	final StateType state;

	final long timestamp;

	final String type;

	final String host;

	public EventEntry(String id, StateType state, long timestamp, String type, String host) {
		this.id = id;
		this.state = state;
		this.timestamp = timestamp;
		this.type = type;
		this.host = host;
	}

	public String getId() {
		return id;
	}

	public StateType getState() {
		return state;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getType() {
		return type;
	}

	public String getHost() {
		return host;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EventEntry that = (EventEntry) o;
		return timestamp == that.timestamp &&
				id.equals(that.id) &&
				state == that.state &&
				Objects.equals(type, that.type) &&
				Objects.equals(host, that.host);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, state, timestamp, type, host);
	}

	@Override
	public String toString() {
		return "EventEntry{" +
				"id='" + id + '\'' +
				", state=" + state +
				", timestamp=" + timestamp +
				", type='" + type + '\'' +
				", host='" + host + '\'' +
				'}';
	}
}
