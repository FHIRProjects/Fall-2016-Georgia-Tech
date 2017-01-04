package edu.gatech.cs6440.teamdna.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConfigUtilsTest {

	@Test
	public void testToBoolean() throws Exception {
		assertTrue(ConfigUtils.toBoolean("y"));
		assertTrue(ConfigUtils.toBoolean("Y"));
		assertTrue(ConfigUtils.toBoolean("yes"));
		assertTrue(ConfigUtils.toBoolean("Yes"));
		assertTrue(ConfigUtils.toBoolean("YES"));
		assertTrue(ConfigUtils.toBoolean("true"));
		assertTrue(ConfigUtils.toBoolean("True"));
		assertTrue(ConfigUtils.toBoolean("TRUE"));
		
		assertFalse(ConfigUtils.toBoolean("n"));
		assertFalse(ConfigUtils.toBoolean("N"));
		assertFalse(ConfigUtils.toBoolean("no"));
		assertFalse(ConfigUtils.toBoolean("No"));
		assertFalse(ConfigUtils.toBoolean("NO"));
		assertFalse(ConfigUtils.toBoolean("false"));
		assertFalse(ConfigUtils.toBoolean("False"));
		assertFalse(ConfigUtils.toBoolean("FALSE"));
		
		assertFalse(ConfigUtils.toBoolean(""));
		assertFalse(ConfigUtils.toBoolean(null));
	}

}
