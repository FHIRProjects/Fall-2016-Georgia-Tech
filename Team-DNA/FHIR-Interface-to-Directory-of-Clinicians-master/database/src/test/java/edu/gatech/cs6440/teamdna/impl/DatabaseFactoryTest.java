package edu.gatech.cs6440.teamdna.impl;
import static org.junit.Assert.*;

import org.junit.Test;

import edu.gatech.cs6440.teamdna.Database;

public class DatabaseFactoryTest {

	@Test
	public void testGetDatabase() throws Exception {
		Database database = DatabaseFactory.getDatabase();
		assertTrue(database instanceof EmbeddedDatabase);
	}

}
