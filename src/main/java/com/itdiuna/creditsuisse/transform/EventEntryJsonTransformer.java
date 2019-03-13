package com.itdiuna.creditsuisse.transform;

import com.itdiuna.creditsuisse.extract.EventEntryJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class EventEntryJsonTransformer {

	private final EventEntryJsonParser parser = new EventEntryJsonParser();

	private final Map<String, EventEntry> pendingEntries = new ConcurrentHashMap<>();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public Optional<Event> transform(EventEntryJson eventEntryJson) {
		EventEntry eventEntry = parser.parse(eventEntryJson);
		logger.debug("Started transformation of entry: {}", eventEntry);
		EventEntry matchedEntry = pendingEntries.putIfAbsent(eventEntry.getId(), eventEntry);
		if (matchedEntry != null) {
			logger.debug("Matched entry");
			pendingEntries.remove(matchedEntry.getId());
			final long startTime;
			final long endTime;
			if (eventEntry.getState() == StateType.STARTED) {
				startTime = eventEntry.getTimestamp();
				endTime = matchedEntry.getTimestamp();
			} else {
				startTime = matchedEntry.getTimestamp();
				endTime = eventEntry.getTimestamp();
			}
			return Optional.of(new Event(
					startTime,
					endTime,
					eventEntry.getId(),
					eventEntry.getType(),
					eventEntry.getHost()
			));
		}
		logger.debug("Marked entry as pending the matching");
		return Optional.empty();
	}
}
