package com.itdiuna.creditsuisse.extract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

public class EventEntryJsonFileExtractor implements AutoCloseable {

	private final BufferedReader reader;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String filePath;

	public EventEntryJsonFileExtractor(String eventsLogFilePath) throws FileNotFoundException {
		filePath = eventsLogFilePath;
		reader = new BufferedReader(new FileReader(filePath));
	}

	public Stream<EventEntryJson> extractLines() {
		logger.info("Started event entries extraction from file {} started", filePath);
		return reader.lines().map(EventEntryJson::new);
	}

	@Override
	public void close() throws IOException {
		reader.close();
		logger.info("File closed");
	}
}
