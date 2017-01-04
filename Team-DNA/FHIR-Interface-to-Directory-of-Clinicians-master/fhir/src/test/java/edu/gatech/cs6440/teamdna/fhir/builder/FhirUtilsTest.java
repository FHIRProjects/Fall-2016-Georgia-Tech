package edu.gatech.cs6440.teamdna.fhir.builder;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.gatech.cs6440.teamdna.fhir.Period;

public class FhirUtilsTest {

	@Test
	public void testAsFhirPeriodStringStringString() throws Exception {
		Period period = FhirUtils.asFhirPeriod(null, "08/16/2016", "08/16/2018");
		assertNotNull(period);
	}

}
