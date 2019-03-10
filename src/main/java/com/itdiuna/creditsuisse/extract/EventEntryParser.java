package com.itdiuna.creditsuisse.extract;

import com.google.gson.Gson;

public class EventEntryParser {

	final Gson gson = new Gson();

	public EventEntry parse(String json) {
		return gson.fromJson(json, EventEntry.class);
	}
}
