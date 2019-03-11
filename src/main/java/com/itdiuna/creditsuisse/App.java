package com.itdiuna.creditsuisse;

import com.itdiuna.creditsuisse.extract.*;
import com.itdiuna.creditsuisse.load.EventDatabaseLoader;
import com.itdiuna.creditsuisse.persistence.EventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class App {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private String eventsLogFilePath;

	App(String eventsLogFilePath) {
		this.eventsLogFilePath = eventsLogFilePath;
	}

	public static void main(String[] args) {
		App app = new App(args[0]);

		app.etl(Executors.newCachedThreadPool());
	}

	private void etl(Executor executor) {
		try (
				EventLineExtractor extractor = new EventLineExtractor(eventsLogFilePath);
				EventDatabaseLoader databaseLoader = new EventDatabaseLoader()
		) {
			String line;
			while ((line = reader.readLine()) != null) { // TODO: verify final solution if reading by multiple better
				String finalLine = line;
				executor.execute(new Runnable() {
					@Override
					public void run() {
						extract(finalLine)
								.map(this::transform)
								.ifPresent(databaseLoader::load);
					}

					private EventEntity transform(Event event) {
						return new EventEntity(
								event.getId(),
								event.getEndTime() - event.getStartTime(),
								event.getType(),
								event.getHost()
						);

					}
					}
				});
			}

		} catch (IOException | SQLException e) {
			logger.error("Unexpected error in extract/transform/load", e);
			e.printStackTrace();
		}
	}
}
