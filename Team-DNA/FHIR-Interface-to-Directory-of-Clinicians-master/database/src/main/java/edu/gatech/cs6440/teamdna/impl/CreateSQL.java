package edu.gatech.cs6440.teamdna.impl;

public final class CreateSQL {
	public static final String CREATE_DOC_PD_MASTER = "CREATE TABLE DBO.DOC_PD_Master "
                                                 + "("
                                                 + "ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                                                 + "DoC_PD_ID varchar(50), "
                                                 + "LICENSE_NO varchar(50), "
                                            	 + "PROFESSION_NAME varchar(50), "
                                            	 + "LICENSE_NAME varchar(50), "
                                            	 + "FIRST_NAME varchar(50), "
                                            	 + "LAST_NAME varchar(50), "
                                            	 + "SORT_NAME varchar(150), "
                                            	 + "FULL_NAME varchar(150), "
                                            	 + "AKA varchar(100), "
                                            	 + "NAME_SUFFIX varchar(50), "
                                            	 + "DATE_OF_BIRTH varchar(50), "
                                            	 + "SSN varchar(50), "
                                            	 + "ISSUE_DATE varchar(50), "
                                            	 + "EXPIRATION_DATE varchar(50), "
                                            	 + "LIC_STATUS varchar(50), "
                                            	 + "DATE_THIS_STATUS varchar(50), "
                                            	 + "ADDR_LINE_1 varchar(100), "
                                            	 + "ADDR_LINE_2 varchar(100), "
                                            	 + "ADDR_CITY varchar(50), "
                                            	 + "ADDR_STATE varchar(50), "
                                            	 + "ADDR_ZIPCODE varchar(50), "
                                            	 + "ADDR_COUNTY varchar(50), "
                                            	 + "ADDR_PHONE varchar(50), "
                                            	 + "ADDR_EMAIL varchar(50), "
                                            	 + "DATE_DECEASED varchar(50), "
                                            	 + "LICENSE_NAME2 varchar(50), "
                                            	 + "DISCIPLINE varchar(50), "
                                            	 + "FEDERAL_ID varchar(50) "
                                                 + ")";
	

	
	public static String[] CREATE_SQL = { 
		CREATE_DOC_PD_MASTER
	};
	
}
