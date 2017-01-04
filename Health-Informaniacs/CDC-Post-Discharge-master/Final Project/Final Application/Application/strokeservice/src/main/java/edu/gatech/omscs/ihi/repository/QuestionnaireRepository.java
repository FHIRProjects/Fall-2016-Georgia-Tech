package edu.gatech.omscs.ihi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.gatech.omscs.ihi.domain.Questionnaire;

@Repository( value = "QuestionnaireRepository" )
public interface QuestionnaireRepository extends CrudRepository< Questionnaire, String >
{
	
}
