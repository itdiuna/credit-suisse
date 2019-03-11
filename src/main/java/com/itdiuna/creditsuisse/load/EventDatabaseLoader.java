package com.itdiuna.creditsuisse.load;

import com.itdiuna.creditsuisse.persistence.EventEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventDatabaseLoader implements AutoCloseable {

	final Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:db");

	public EventDatabaseLoader() throws SQLException {
	}

	public void load(EventEntity eventEntity) {
		try (PreparedStatement statement = connection.prepareStatement("insert into events(?,?,?)")) {
			statement.setString(1, eventEntity.getId());
			statement.setLong(2, eventEntity.getDuration());
			statement.setString(3, eventEntity.getType());
			statement.setString(4, eventEntity.getHost());
			statement.setBoolean(5, eventEntity.getAlert());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void close() throws SQLException {
		connection.close();
	}
}
