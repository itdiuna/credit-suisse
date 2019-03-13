package com.itdiuna.creditsuisse;

import com.itdiuna.creditsuisse.extract.EventEntryJsonFileExtractor;
import com.itdiuna.creditsuisse.load.EventDatabaseLoader;
import com.itdiuna.creditsuisse.transform.EventEntryJsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class App {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String eventsLogFilePath;

	private App(String eventsLogFilePath) {
		this.eventsLogFilePath = eventsLogFilePath;
	}

	public static void main(String[] args) {
		App app = new App(args[0]);

		app.etl(Executors.newCachedThreadPool());
	}

	private void etl(Executor executor) {
		EventEntryJsonTransformer transformer = new EventEntryJsonTransformer();
		try (
				EventEntryJsonFileExtractor extractor = new EventEntryJsonFileExtractor(eventsLogFilePath);
				EventDatabaseLoader databaseLoader = new EventDatabaseLoader()
		) {
			extractor.extractLines().forEach(line -> {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						transformer.transform(line).ifPresent(databaseLoader::load);
					}
				});
			});
		} catch (IOException | SQLException e) {
			logger.error("Unexpected error in extract/transform/load", e);
		}
	}
}
