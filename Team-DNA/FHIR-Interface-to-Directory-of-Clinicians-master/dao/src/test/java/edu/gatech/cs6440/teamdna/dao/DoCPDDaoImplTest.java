package edu.gatech.cs6440.teamdna.dao;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.gatech.cs6440.teamdna.model.DoCPDDo;
import static edu.gatech.cs6440.teamdna.dao.DoCPDDaoConstants.*;

public class DoCPDDaoImplTest {

	private DoCPDDao underTest = new DoCPDDaoImpl();
	
	@Test
	public void testFindByLicenseNo() throws Exception {
		DoCPDDo docPD = getDocPD();
		int count = underTest.save(docPD);
		assertTrue(count == 1);
		List<DoCPDDo> actual = underTest.findByLicenseNo("LICENSE_NO");
		assertNotNull(actual);
		assertTrue(actual.size() >= 1);
		
		for (DoCPDDo doCPD2 : actual) {
			underTest.delete(doCPD2);
		}
	}
		
	@Test
	public void testFindByLastName() throws Exception {
		DoCPDDo docPD = getDocPD();
		int count = underTest.save(docPD);
		assertTrue(count == 1);
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		parameters.put(LAST_NAME, Arrays.asList(new String[]{"Last_Name"}));

		List<DoCPDDo> actual = underTest.find(parameters);
		assertNotNull(actual);
		assertTrue(actual.size() >= 1);
		
		for (DoCPDDo doCPD2 : actual) {
			underTest.delete(doCPD2);
		}
	}

	@Test
	public void testFind_MultipleColumns() throws Exception {
		DoCPDDo docPD = getDocPD();
		int count = underTest.save(docPD);
		assertTrue(count == 1);
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		parameters.put(LICENSE_NO, Arrays.asList(new String[]{"LICENSE_NO"}));
		parameters.put(LAST_NAME, Arrays.asList(new String[]{"LAST_NAME"}));

		List<DoCPDDo> actual = underTest.find(parameters);
		assertNotNull(actual);
		assertTrue(actual.size() >= 1);
		
		for (DoCPDDo doCPD2 : actual) {
			underTest.delete(doCPD2);
		}
	}
	
	@Test
	public void testFind_MultipleColumnsMultipleValues() throws Exception {
		DoCPDDo docPD = getDocPD();
		int count = underTest.save(docPD);
		assertTrue(count == 1);
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		parameters.put(LICENSE_NO, Arrays.asList(new String[]{"LICENSE_NO"}));
		parameters.put(LAST_NAME, Arrays.asList(new String[]{"LAST_NAME", "ANOTHER_LAST_NAME"}));

		List<DoCPDDo> actual = underTest.find(parameters);
		assertNotNull(actual);
		assertTrue(actual.size() >= 1);
		
		for (DoCPDDo doCPD2 : actual) {
			underTest.delete(doCPD2);
		}
	}
	
	@Test
	public void testFind_InvalidColumns() throws Exception {
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		parameters.put("NOT_A_REAL_COLUMN_NAME", Arrays.asList(new String[]{"SOME_VALUE"}));

		List<DoCPDDo> actual = underTest.find(parameters);
		assertNotNull(actual);
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testSave_Insert() throws Exception {
		DoCPDDo docPD = getDocPD();
		assertTrue(underTest.save(docPD) == 1);
	}
	
	private DoCPDDo getDocPD(){
		DoCPDDo docPD = new DoCPDDo();
		docPD.setAddrCity("ADDR_CITY");
		docPD.setAddrCounty("ADDR_COUNTY");
		docPD.setAddrEmail("ADDR_EMAIL");
		docPD.setAddrLine1("ADDR_LINE_1");
		docPD.setAddrLine2("ADDR_LINE_2");
		docPD.setAddrPhone("ADDR_PHONE");
		docPD.setAddrState("ADDR_STATE");
		docPD.setAddrZipcode("ADDR_ZIPCODE");
		docPD.setAka("AKA");
		docPD.setDateDeceased("DATE_DECEASED");
		docPD.setDateOfBirth("DATE_OF_BIRTH");
		docPD.setDateThisStatus("DATE_THIS_STATUS");
		docPD.setDiscipline("DISCIPLINE");
		docPD.setDocPdId("DoC_PD_ID");
		docPD.setExpirationDate("EXPIRATION_DATE");
		docPD.setFederalId("FEDERAL_ID");
		docPD.setFirstName("FIRST_NAME");
		docPD.setFullName("FULL_NAME");
		docPD.setIssueDate("ISSUE_DATE");
		docPD.setLastName("LAST_NAME");
		docPD.setLicenseName("LICENSE_NAME");
		docPD.setLicenseName2("LICENSE_NAME2");
		docPD.setLicenseNo("LICENSE_NO");
		docPD.setLicStatus("LIC_STATUS");
		docPD.setNameSuffix("NAME_SUFFIX");
		docPD.setProfessionName("PROFESSION_NAME");
		docPD.setSortName("SORT_NAME");
		docPD.setSsn("SSN");
		
		return docPD;
	}
}
