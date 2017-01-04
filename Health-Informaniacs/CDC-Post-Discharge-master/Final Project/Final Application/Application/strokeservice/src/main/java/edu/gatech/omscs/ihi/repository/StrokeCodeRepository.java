package edu.gatech.omscs.ihi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.gatech.omscs.ihi.domain.StrokeCode;

@Repository( value = "StrokeCodeRepository" )
public interface StrokeCodeRepository extends CrudRepository< StrokeCode, String >
{
	
}