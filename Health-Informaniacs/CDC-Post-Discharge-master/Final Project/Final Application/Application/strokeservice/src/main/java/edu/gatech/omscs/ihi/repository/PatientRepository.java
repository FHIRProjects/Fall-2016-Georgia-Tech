package edu.gatech.omscs.ihi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.gatech.omscs.ihi.domain.Patient;
import edu.gatech.omscs.ihi.domain.id.PatientId;

@Repository( value = "PatientRepository" )
public interface PatientRepository extends CrudRepository< Patient, PatientId >
{
	@Query( "SELECT p FROM Patient p WHERE destination_id = ?1" )
	public Iterable< Patient > findAllPatientsByDestinationId( String destinationId );	
}