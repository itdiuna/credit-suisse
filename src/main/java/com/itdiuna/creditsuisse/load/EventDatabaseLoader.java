package com.itdiuna.creditsuisse.load;

import com.itdiuna.creditsuisse.transform.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventDatabaseLoader implements AutoCloseable {

	private final Connection connection;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public EventDatabaseLoader() {
		try {
			logger.info("Creating connection to database");
			connection = DriverManager.getConnection("jdbc:hsqldb:file:db");
			logger.info("Connection established");
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to database", e);
		}
	}

	public void load(Event event) {
		logger.debug("Inserting event: {}", event);
		long eventDuration = event.getEndTime() - event.getStartTime();
		try (PreparedStatement statement = connection.prepareStatement("insert into events(?,?,?)")) {
			statement.setString(1, event.getId());
			statement.setLong(2, eventDuration);
			statement.setString(3, event.getType());
			statement.setString(4, event.getHost());
			statement.setBoolean(5, eventDuration > 4L);
			statement.execute();
		} catch (SQLException e) {
			logger.error("Could not load the event into database", e);
		}
	}

	@Override
	public void close() throws SQLException {
		connection.close();
	}
}
