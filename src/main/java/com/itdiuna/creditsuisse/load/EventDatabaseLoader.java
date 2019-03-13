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

	public EventDatabaseLoader(String databaseUrl) throws SQLException {
		logger.info("Creating connection to database");
		connection = DriverManager.getConnection(databaseUrl);
		logger.info("Connection established");
		connection.createStatement().execute(
				"create table if not exists events (" +
						"id varchar(255) primary key," +
						"duration integer," +
						"type varchar(255)," +
						"host varchar(255)," +
						"alert boolean" +
						")"
		);
		logger.info("Table events created (if not existed)");
	}

	public void load(Event event) {
		logger.debug("Inserting event: {}", event);
		long eventDuration = event.getEndTime() - event.getStartTime();
		try (PreparedStatement statement = connection.prepareStatement("insert into events values (?,?,?,?,?)")) {
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
		logger.info("Connection closed");
	}
}
