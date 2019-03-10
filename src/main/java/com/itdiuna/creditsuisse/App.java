package com.itdiuna.creditsuisse;

import com.google.gson.Gson;
import com.itdiuna.creditsuisse.extract.Event;
import com.itdiuna.creditsuisse.extract.EventEntry;
import com.itdiuna.creditsuisse.extract.EventEntryParser;
import com.itdiuna.creditsuisse.extract.StateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

	final Logger logger = LoggerFactory.getLogger(getClass());

	final Map<String, Event> events = new ConcurrentHashMap<>();
	private String eventsLogFilePath;
	final EventEntryParser parser = new EventEntryParser();

	App(String eventsLogFilePath) {
		this.eventsLogFilePath = eventsLogFilePath;
	}

	public static void main(String[] args) {
		App app = new App(args[0]);

		app.etl(Executors.newCachedThreadPool());
	}

	private void etl(Executor executor) {
		try(BufferedReader reader = new BufferedReader(new FileReader(eventsLogFilePath))) {
			String line;
			while ((line = reader.readLine()) != null) { // TODO: verify final solution if reading by multiple better
				String finalLine = line;
				executor.execute(new Runnable() {
					@Override
					public void run() {
						EventEntry eventEntry = parser.parse(finalLine);
						Event event = events.putIfAbsent(
								eventEntry.getId(),
								new Event(
										eventEntry.getState() == StateType.STARTED ?
												eventEntry.getTimestamp() :
												Long.MIN_VALUE,
										eventEntry.getState() == StateType.FINISHED ?
												eventEntry.getTimestamp() :
												Long.MAX_VALUE,
										eventEntry.getId(),
										eventEntry.getType(),
										eventEntry.getHost()
								));
						if (event != null) {
//							Event completeEvent = new Event()
						}
					}
				});
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
