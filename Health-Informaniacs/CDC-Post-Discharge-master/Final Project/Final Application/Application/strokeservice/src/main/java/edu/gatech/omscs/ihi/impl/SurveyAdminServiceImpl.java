package edu.gatech.omscs.ihi.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.gatech.omscs.ihi.domain.StrokeCode;
import edu.gatech.omscs.ihi.domain.SurveyAdmin;
import edu.gatech.omscs.ihi.repository.DestinationRepository;
import edu.gatech.omscs.ihi.repository.StrokeCodeRepository;
import edu.gatech.omscs.ihi.repository.SurveyAdminRepository;
import edu.gatech.omscs.ihi.service.SurveyAdminService;

@Service( value = "SurveyAdminService" )
public class SurveyAdminServiceImpl implements SurveyAdminService 
{
	@Value( "${admin.password}" )
	private String adminPassword;
	
	@Autowired
	private SurveyAdminRepository surveyAdminRepository;
	
	@Autowired
	private DestinationRepository destinationRepository;
	
	@Autowired
	private StrokeCodeRepository strokeCodesRepository;
	
	@Override
	public boolean authenticate( String username, String password ) 
	{
		if ( username != null && password != null )
		{
			if ( username.equalsIgnoreCase( "admin" ) )
			{
				return password.equalsIgnoreCase( adminPassword );
			}
			else
			{
				SurveyAdmin surveyAdmin = this.surveyAdminRepository.findOne( username );
				
				if ( surveyAdmin != null )
				{
					return ( surveyAdmin.getUsername().equals( username ) && surveyAdmin.getPassword().equals( password ) );
				}
			}
		}
		
		return false;
	}
	
	@Override
	public int getDestinationForSurveyAdmin( String username ) 
	{	
		SurveyAdmin surveyAdmin = this.surveyAdminRepository.findOne( username ); 
		
		int adminId = surveyAdmin.getSurveyAdminId();
		
		return this.destinationRepository.findDestinationIdByAdminId( adminId );
	}
	
	@Override
	public Iterable< StrokeCode > getStrokeCodes()
	{
		return strokeCodesRepository.findAll();
	}
}