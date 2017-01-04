package edu.gatech.omscs.ihi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.gatech.omscs.ihi.domain.SurveyAdmin;

@Repository( value = "SurveyAdminRepository" )
public interface SurveyAdminRepository extends CrudRepository< SurveyAdmin, String >
{
	
}