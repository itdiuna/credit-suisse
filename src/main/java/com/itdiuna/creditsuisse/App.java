package com.itdiuna.creditsuisse;

import com.itdiuna.creditsuisse.extract.EventEntryJsonFileExtractor;
import com.itdiuna.creditsuisse.load.EventDatabaseLoader;
import com.itdiuna.creditsuisse.transform.EventEntryJsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class App {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String eventsLogFilePath;

	private App(String eventsLogFilePath) {
		this.eventsLogFilePath = eventsLogFilePath;
	}

	public static void main(String[] args) {
		App app = new App(args[0]);

		ExecutorService executor = Executors.newWorkStealingPool();
		app.etl(executor);
		executor.shutdown();
	}

	private void etl(ExecutorService executor) {
		EventEntryJsonTransformer transformer = new EventEntryJsonTransformer();
		try (
				EventEntryJsonFileExtractor extractor = new EventEntryJsonFileExtractor(eventsLogFilePath);
				EventDatabaseLoader databaseLoader = new EventDatabaseLoader("jdbc:hsqldb:file:db")
		) {
			List<? extends Future<?>> futures = extractor
					.extractLines()
					.map(line -> executor.submit(() -> transformer.transform(line).ifPresent(databaseLoader::load)))
					.collect(Collectors.toList());
			futures
					.forEach(f -> {
						try {
							f.get();
						} catch (InterruptedException | ExecutionException e) {
							throw new RuntimeException(e);
						}
					});
			logger.info("Events loaded from file into database successfully.");
		} catch (IOException | SQLException e) {
			logger.error("Unexpected error in extract/transform/load", e);
		}
	}
}
