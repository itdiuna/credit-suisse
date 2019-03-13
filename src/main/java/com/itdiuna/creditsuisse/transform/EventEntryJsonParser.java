package com.itdiuna.creditsuisse.transform;

import com.google.gson.Gson;
import com.itdiuna.creditsuisse.extract.EventEntryJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventEntryJsonParser {

	private final Gson gson = new Gson();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public EventEntry parse(EventEntryJson eventEntryJson) {
		logger.debug("Parsing event entry: {}", eventEntryJson.getJson());
		return gson.fromJson(eventEntryJson.getJson(), EventEntry.class);
	}
}
