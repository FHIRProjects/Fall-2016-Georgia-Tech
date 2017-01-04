package edu.gatech.cs6440.teamdna.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import edu.gatech.cs6440.teamdna.business.DoCManager;
import edu.gatech.cs6440.teamdna.model.DoCPDDo;

@Component
@Path("bulk")
public class DoCBulkUploadService {
	private static final String ADDR_CITY = "ADDR_CITY";
	private static final String ADDR_COUNTY = "ADDR_COUNTY";
	private static final String ADDR_EMAIL = "ADDR_EMAIL";
	private static final String ADDR_LINE_1 = "ADDR_LINE_1";
	private static final String ADDR_LINE_2 = "ADDR_LINE_2";
	private static final String ADDR_PHONE = "ADDR_PHONE";
	private static final String ADDR_STATE = "ADDR_STATE";
	private static final String ADDR_ZIPCODE = "ADDR_ZIPCODE";
	private static final String AKA = "AKA";
	private static final String DATE_DECEASED = "DATE_DECEASED";
	private static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";
	private static final String DATE_THIS_STATUS = "DATE_THIS_STATUS";
	private static final String DISCIPLINE = "DISCIPLINE";
	private static final String DoC_PD_ID = "DoC_PD_ID";
	private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
	private static final String FEDERAL_ID = "FEDERAL_ID";
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String FULL_NAME = "FULL_NAME";
	private static final String ISSUE_DATE = "ISSUE_DATE";
	private static final String LAST_NAME = "LAST_NAME";
	private static final String LICENSE_NAME = "LICENSE_NAME";
	private static final String LICENSE_NAME2 = "LICENSE_NAME2";
	private static final String LICENSE_NO = "LICENSE_NO";
	private static final String LIC_STATUS = "LIC_STATUS";
	private static final String NAME_SUFFIX = "NAME_SUFFIX";
	private static final String PROFESSION_NAME = "PROFESSION_NAME";
	private static final String SORT_NAME = "SORT_NAME";
	private static final String SSN = "SSN";
	
	@Autowired
	private DoCManager docManager;
	
	public void setDocManager(DoCManager docManager) {
		this.docManager = docManager;
	}
	
	@POST
	@Path("addCsvFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addNew(	@FormDataParam("file") InputStream fileInputStream,
							@FormDataParam("file") FormDataContentDisposition contentDispositionHeader){
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
		try {
            CSVParser csvParser = new CSVParser(br, CSVFormat.EXCEL.withFirstRecordAsHeader());
            try{
                    List<CSVRecord> list = csvParser.getRecords();

                    for (CSVRecord csvRecord : list) {
                            DoCPDDo doCPD = buildDoCPD(csvRecord);
                            docManager.addNew(doCPD);
                    }
            } finally {
                    csvParser.close();
            }

			return Response.ok("SUCCESS").build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.ok("ERROR:" + e.getMessage()).build();
		}
	}


    private DoCPDDo buildDoCPD(CSVRecord bulkLineData) {
		DoCPDDo docPD = new DoCPDDo();
		docPD.setAddrCity( bulkLineData.get( ADDR_CITY ) );
		docPD.setAddrCounty( bulkLineData.get( ADDR_COUNTY ) );
		docPD.setAddrEmail( bulkLineData.get( ADDR_EMAIL ) );
		docPD.setAddrLine1( bulkLineData.get( ADDR_LINE_1 ) );
		docPD.setAddrLine2( bulkLineData.get( ADDR_LINE_2 ) );
		docPD.setAddrPhone( bulkLineData.get( ADDR_PHONE ) );
		docPD.setAddrState( bulkLineData.get( ADDR_STATE ) );
		docPD.setAddrZipcode( bulkLineData.get( ADDR_ZIPCODE ) );
		docPD.setAka( bulkLineData.get( AKA ) );
		docPD.setDateDeceased( bulkLineData.get( DATE_DECEASED ) );
		docPD.setDateOfBirth( bulkLineData.get( DATE_OF_BIRTH ) );
		docPD.setDateThisStatus( bulkLineData.get( DATE_THIS_STATUS ) );
		docPD.setDiscipline( bulkLineData.get( DISCIPLINE ) );
		docPD.setDocPdId( bulkLineData.get( DoC_PD_ID ) );
		docPD.setExpirationDate( bulkLineData.get( EXPIRATION_DATE ) );
		docPD.setFederalId( bulkLineData.get( FEDERAL_ID ) );
		docPD.setFirstName( bulkLineData.get( FIRST_NAME ) );
		docPD.setFullName( bulkLineData.get( FULL_NAME ) );
		docPD.setIssueDate( bulkLineData.get( ISSUE_DATE ) );
		docPD.setLastName( bulkLineData.get( LAST_NAME ) );
		docPD.setLicenseName( bulkLineData.get( LICENSE_NAME ) );
		docPD.setLicenseName2( bulkLineData.get( LICENSE_NAME2 ) );
		docPD.setLicenseNo( bulkLineData.get( LICENSE_NO ) );
		docPD.setLicStatus( bulkLineData.get( LIC_STATUS ) );
		docPD.setNameSuffix( bulkLineData.get( NAME_SUFFIX ) );
		docPD.setProfessionName( bulkLineData.get( PROFESSION_NAME ) );
		docPD.setSortName( bulkLineData.get( SORT_NAME ) );
		docPD.setSsn( bulkLineData.get( SSN ) );
		
		return docPD;
	}
}
