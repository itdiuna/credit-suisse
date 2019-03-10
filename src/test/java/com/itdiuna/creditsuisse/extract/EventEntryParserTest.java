package com.itdiuna.creditsuisse.extract;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventEntryParserTest {

	EventEntryParser parser = new EventEntryParser();

	@Test
	public void parse() {
		String json = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"" +
				"12345\", \"timestamp\":1491377495212}";

		EventEntry eventEntry = parser.parse(json);

		assertEquals(
				new EventEntry(
						"scsmbstgra",
						StateType.STARTED,
						1491377495212L,
						"APPLICATION_LOG",
						"12345"
				),
				eventEntry
		);
	}
}