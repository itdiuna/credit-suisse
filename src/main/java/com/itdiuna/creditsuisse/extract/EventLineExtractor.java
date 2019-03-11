package com.itdiuna.creditsuisse.extract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class EventLineExtractor implements AutoCloseable {

	final Map<String, Event> events = new ConcurrentHashMap<>();

	final EventEntryParser parser = new EventEntryParser();

	final BufferedReader reader;

	final Logger logger = LoggerFactory.getLogger(getClass());

	public EventLineExtractor(String eventsLogFilePath) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(eventsLogFilePath));
	}

	public Optional<Event> extract(String line) {
		EventEntry eventEntry = parser.parse(line);
		long startTime = eventEntry.getState() == StateType.STARTED ?
				eventEntry.getTimestamp() :
				Long.MIN_VALUE;
		long endTime = eventEntry.getState() == StateType.FINISHED ?
				eventEntry.getTimestamp() :
				Long.MAX_VALUE;
		Event event = events.putIfAbsent(
				eventEntry.getId(),
				new Event(
						startTime,
						endTime,
						eventEntry.getId(),
						eventEntry.getType(),
						eventEntry.getHost()
				)
		);
		if (event != null) {
			events.remove(eventEntry.getId());
			Event completeEvent = new Event(
					Math.max(startTime, event.getStartTime()),
					Math.min(endTime, event.getEndTime()),
					event.getId(),
					event.getType(),
					event.getHost()
			);
			logger.debug("event: {}", completeEvent);
			return Optional.of(completeEvent);
		}
		return Optional.empty();
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
