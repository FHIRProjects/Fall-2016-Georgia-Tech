package edu.gatech.omscs.ihi.controller;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.context.FhirContext;

import ca.uhn.fhir.model.dstu2.resource.QuestionnaireResponse;
import ca.uhn.fhir.model.dstu2.resource.QuestionnaireResponse.Group;
import ca.uhn.fhir.model.dstu2.resource.QuestionnaireResponse.GroupQuestion;

import ca.uhn.fhir.model.primitive.StringDt;
import edu.gatech.omscs.ihi.domain.Patient;
import edu.gatech.omscs.ihi.domain.StrokeCode;
import edu.gatech.omscs.ihi.domain.id.PatientId;
import edu.gatech.omscs.ihi.domain.Questionnaire;
import edu.gatech.omscs.ihi.repository.PatientRepository;
import edu.gatech.omscs.ihi.repository.QuestionnaireRepository;
import edu.gatech.omscs.ihi.service.FhirResourceService;
import edu.gatech.omscs.ihi.service.ServerConnectionService;
import edu.gatech.omscs.ihi.service.SurveyAdminService;
import edu.gatech.omscs.ihi.util.AnswerPayload;
import edu.gatech.omscs.ihi.util.JsonUtils;

@RestController
@RequestMapping( value = "cdc/api/stroke" )
public class StrokeController
{
	@Autowired
	private ServerConnectionService serverConnectionService;
	
	@Autowired 
	private PatientRepository patientRepository;
	
	@Autowired
	private SurveyAdminService surveyAdminService;
	
	@Autowired
	private FhirResourceService fhirResource;
	
	@Autowired 
	private QuestionnaireRepository questionnaireRepository;
	
	private boolean checkCredentials( HttpServletRequest request)
	{
		String username = request.getHeader( "username" );
		String password = request.getHeader( "password" );
		
		return this.surveyAdminService.authenticate( username, password );
	}
	
	// ***********************************
	// ***********************************
	// USER AUTHENTICATION
	// ***********************************
	// ***********************************
	
	@RequestMapping( value = "authenticate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public JsonNode authenticate( HttpServletRequest request )
	{
		String value = checkCredentials( request ) ? "Success" : "Failure";
		
		return JsonUtils.convertKeyValueToJson( "AuthenticationSuccess", value );
	}
	
	// ***********************************
	// ***********************************
	// PATIENT
	// ***********************************
	// ***********************************
	
	@RequestMapping( value = "patient", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public JsonNode getPatients( HttpServletRequest request )
	{
		if ( checkCredentials( request ) )
		{
			int destId = this.surveyAdminService.getDestinationForSurveyAdmin( request.getHeader( "username" ) );
			String strDestId = new Integer(destId).toString();
			
			return JsonUtils.convertBeanToJsonNode( this.patientRepository.findAllPatientsByDestinationId( strDestId ) );
		}
		
		return null;
	}
	
	@RequestMapping( value = "patient", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public void setPatient( HttpServletRequest request, @RequestBody JsonNode dischargedPatients )
	{
		Patient toPublish = ( Patient ) JsonUtils.convertJsonToBean( dischargedPatients.toString(), Patient.class );				
		this.patientRepository.save( toPublish );
	}
	
	// ***********************************
	// ***********************************
	// STROKE CODES
	// ***********************************
	// ***********************************
	
	@RequestMapping( value = "codes/icd", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public JsonNode getStrokeCodes( HttpServletRequest request)
	{		
		Iterable< StrokeCode > codes = surveyAdminService.getStrokeCodes();
		
		return JsonUtils.convertBeanToJsonNode( codes );	
	}
	
	// ***********************************
	// ***********************************
	// QUESTIONNAIRE
	// ***********************************
	// ***********************************
	
	@RequestMapping( value = "questionnaire", method = RequestMethod.POST )
	public void uploadQuestionnaire( HttpServletRequest request, @RequestParam( "file" ) MultipartFile file )
	{
		if ( checkCredentials( request) )
		{
			try
			{
				String testId = file.getOriginalFilename();
				testId = file.getOriginalFilename().substring( 0, testId.lastIndexOf( "." ) );
				
				ByteArrayInputStream stream = new ByteArrayInputStream( file.getBytes() );
				
				String json_resource = IOUtils.toString( stream, "UTF-8" );
				
				// Save Questionnaire to FHIR
				String fhirId = this.fhirResource.updateResource("Questionnaire", json_resource);
				
				// Save Questionnaire to localdb
				this.questionnaireRepository.save( new Questionnaire( fhirId, json_resource ) );
				
			}
			catch ( Exception exp )
			{
				exp.printStackTrace();
			}
		}
	}
	
	@RequestMapping( value = "questionnaire", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public JsonNode getQuestionnaireJson( HttpServletRequest request )
	{
		if ( checkCredentials( request) )
		{			
			return JsonUtils.convertBeanToJsonNode( this.questionnaireRepository.findAll() );
		}
		
		return null;
	}
	
	@RequestMapping( value = "questionnaire/{id}", method = RequestMethod.DELETE )
	public void deleteQuestionnaire( HttpServletRequest request, @PathVariable( "id" ) String questionnaireId )
	{
		// Delete from strokeapp db
		System.out.println("delete questionnaire called with id: " + questionnaireId);
		if ( checkCredentials( request) )
		{
			this.questionnaireRepository.delete( questionnaireId );
		}
		
		this.fhirResource.deleteFhirResource("Questionnaire", questionnaireId);
		
	}
	
	// ***********************************
	// ***********************************
	// QUESTIONNAIRE RESPONSE
	// ***********************************
	// ***********************************
	@RequestMapping( value = "questionnaire-response", method = RequestMethod.GET )
	public JsonNode getQuestionnaireResponse( HttpServletRequest request, @PathVariable( "id" ) String questionnaireId )
	{
		return null;
	}
	
	
	
	@RequestMapping( value = "questionnaire-response", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public void uploadQuestionnaireResponse( HttpServletRequest request, 
			@RequestParam( "patientid" ) String patientId ,
			@RequestParam( "encounterid" ) String encounterId ,
			@RequestParam( "destinationid" ) String destinationId ,
			@RequestBody JsonNode answers)
	{
		if ( checkCredentials( request) )
		{
			try
			{				
				//System.out.println(answers.toString());
				
				Map<String,AnswerPayload> answerMap = new LinkedHashMap<String,AnswerPayload>(); 				
				
				// Serialize json to Map
				ObjectMapper mapperObj = new ObjectMapper();
				
				answerMap = mapperObj.readValue(answers.toString(),
                        new TypeReference<LinkedHashMap<String,AnswerPayload>>(){});
				
				// Set up the QuestionnaireResponse by first getting the Questionnaire 
				// FHIR ID
				Iterable<Questionnaire> surveys = this.questionnaireRepository.findAll();
				Iterator<Questionnaire> itr = surveys.iterator();
				Questionnaire survey =  itr.next();
				
				//System.out.println("Questionnaire id: " + survey.getId());
				
				// Set the id on the QuestionnaireResponse
				QuestionnaireResponse qr = new QuestionnaireResponse();
				qr.getQuestionnaire().setReference("Questionnaire/" + survey.getId());
				qr.getGroup().setLinkId("root");
				
				qr.getSubject().setReference("Patient/" + patientId); 					
				
				// Iterate through the AnswerPayload and construct the QuestionnaireResponse
				ArrayList<AnswerPayload> vals = new ArrayList<AnswerPayload>(answerMap.values());
				
				Group toAdd = null;
				HashMap<String,Group> groupCache = new HashMap<String,Group>();
				StringBuffer csv = new StringBuffer();
				
				// csv header
				csv.append("Group name,LinkId,Question,Answer\n");
				
				// construct the QuestionnaireResponse, maybe generate CSV here				
				for (int i = 0 ; i < vals.size() ; i++)
				{
					AnswerPayload current = vals.get(i);
					String linkId = current.getId();
					String text = current.getText().trim();
					String field = current.getField();
					
					if (field.equals("group") && !linkId.contains("."))
					{
						toAdd = qr.getGroup().addGroup();
						toAdd.setLinkId(linkId);
						toAdd.setTitle(text);
						groupCache.put(linkId, toAdd);
						
					}
					else if (!field.equals("group"))
					{
						// First char of question represents its group
						int dot = linkId.indexOf(".");
						String groupKey = linkId.substring(0, dot);

						Group toUpdate = groupCache.get(groupKey);
						GroupQuestion question = toUpdate.addQuestion();
						question.setLinkId(linkId);
						question.setText(text);
						
						question.addAnswer().setValue(new StringDt(current.getValue()));
						csv.append("\"" + toUpdate.getTitle() +
								   "\"," + linkId + 
								   ",\"" + text + 
								   "\",\"" + 
								   current.getValue() + "\"\n");
						
					}					
				}	
								
				FhirContext ctx = this.serverConnectionService.getFhirContext();
				String encoded = ctx.newJsonParser().encodeResourceToString(qr);
			    //System.out.println("====QR====\n" + encoded);
			    
			    // Push QuestionnaireResponse to FHIR
			    String qrId = this.fhirResource.updateResource("QuestionnaireResponse", encoded);
			    
			    // getting strokapp patient given mrn
			    Patient toUpdate = this.patientRepository.findOne(new PatientId(patientId, encounterId, destinationId));
			    
			    toUpdate.setQuestionnaireResponseId(qrId);
			    toUpdate.setQuestionnaireResponseJson(encoded);
			    toUpdate.setQuestionnaireResponseCsv(csv.toString());
			    this.patientRepository.save(toUpdate);
				
			}
			catch ( Exception exp )
			{
				exp.printStackTrace();
			}
		}
	}
	
	// ***********************************
	// ***********************************
	// FHIR
	// ***********************************
	// ***********************************
	
	@RequestMapping( value = "fhir", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public JsonNode getFhirResource( HttpServletRequest request, @PathVariable( "resourceType" ) String resourceType, @PathVariable( "resourceId" ) String resourceId )
	{
		if ( checkCredentials( request) )
		{
			this.fhirResource.getFhirResource( resourceType, resourceId );
		}
		
		return null;
	}
	
	@RequestMapping( value = "fhir", method = RequestMethod.POST )
	public void updateFhirResource( HttpServletRequest request, @PathVariable( "resourceType" ) String resourceType, @RequestBody JsonNode resource )
	{
		if ( checkCredentials( request) )
		{
			this.fhirResource.updateResource( resourceType, resource.toString() );
		}
	}
	
	@RequestMapping( value = "fhir", method = RequestMethod.DELETE )
	public void deleteFhirResource( HttpServletRequest request, @PathVariable( "resourceType" ) String resourceType, @PathVariable( "resourceId" ) String resourceId )
	{
		if ( checkCredentials( request) )
		{
			
		}
	}
	
	// ***********************************
	// ***********************************
	// ADMIN
	// ***********************************
	// ***********************************
	
	@RequestMapping( value = "admin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public JsonNode getAdminJson( HttpServletRequest request )
	{
		if ( checkCredentials( request) )
		{
			
		}
		
		return null;
	}
	
	@RequestMapping( value = "admin", method = RequestMethod.POST )
	public void uploadAdminJson( HttpServletRequest request, @RequestParam( "file" ) MultipartFile file )
	{
		if ( checkCredentials( request) )
		{
			
		}
	}
	
	@RequestMapping( value = "admin", method = RequestMethod.DELETE )
	public void deleteAdminJson( HttpServletRequest request, @PathVariable( "id" ) String testId )
	{
		if ( checkCredentials( request) )
		{
			
		}
	}
	
	// ***********************************
	// ***********************************
	// TEST
	// ***********************************
	// ***********************************
/*	
	@RequestMapping( value = "test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public JsonNode getTestJson( HttpServletRequest request )
	{
		getQR();
		System.out.println("Called QR");
		if ( checkCredentials( request) )
		{
			return JsonUtils.convertBeanToJsonNode( this.testRepository.findAll() );
		}
		
		
		
		return null;
	}
	
	@RequestMapping( value = "test", method = RequestMethod.POST )
	public void uploadTestJson( HttpServletRequest request, @RequestParam( "file" ) MultipartFile file )
	{
		if ( checkCredentials( request) )
		{
			try
			{
				String testId = file.getOriginalFilename();
				testId = file.getOriginalFilename().substring( 0, testId.lastIndexOf( "." ) );
				
				ByteArrayInputStream stream = new ByteArrayInputStream( file.getBytes() );
				this.testRepository.save( new Questionnaire( testId, IOUtils.toString( stream, "UTF-8" ) ) );
			}
			catch ( Exception exp )
			{
				exp.printStackTrace();
			}
		}
	}
	
	@RequestMapping( value = "test/{id}", method = RequestMethod.DELETE )
	public void deleteTestJson( HttpServletRequest request, @PathVariable( "id" ) String testId )
	{
		if ( checkCredentials( request) )
		{
			this.testRepository.delete( testId );
		}
	}
	
	
	public void getQR()
	{
			System.out.println("Enter QR)");
			
			Test test =  this.testRepository.findOne("questionnaire-response");
			JsonNode newnode= JsonUtils.convertBeanToJsonNode( test.getJson() );
			System.out.println(newnode.toString());
			QuestionnaireResponse qr = FhirContext.forDstu2().newJsonParser().parseResource( QuestionnaireResponse.class, newnode.toString());
			System.out.println("Created QR, calling convert");
			
			convertJsontoCSV(qr);
	}
	
	public void convertJsontoCSV(QuestionnaireResponse response) 
	{
		
		//response.getGroup().isEmpty();
		System.out.println("entered convert");
		
		Iterator<?> it = (Iterator<?>) response.getGroup();

		while(it.hasNext())
		{
			Group group = (Group) it;
			if (group.isEmpty())
			{
				 String groupTitle = group.getTitle();
				 System.out.println("Getting group.questions");
				 process_questions(group.getQuestion());
				
				
			}
			
			
		}
	}

	public void process_questions(List<GroupQuestion> questions)
	{
		System.out.println("entered questions");
				Iterator<?> it = (Iterator<?>) questions;
				
				while(it.hasNext())
				{
					GroupQuestion question = (GroupQuestion) it.next();
					String answers = question.getText();
					System.out.println(answers);
					
				}
				
	}
	*/
		
}
