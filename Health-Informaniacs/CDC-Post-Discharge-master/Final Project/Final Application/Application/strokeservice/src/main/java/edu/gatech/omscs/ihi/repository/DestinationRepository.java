package edu.gatech.omscs.ihi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.gatech.omscs.ihi.domain.Destination;


@Repository( value = "DestinationRepository" )
public interface DestinationRepository extends CrudRepository< Destination, String >
{
	@Query( "SELECT destinationId FROM Destination WHERE surveyAdminId = ?1" )
	public int findDestinationIdByAdminId( int surveyAdminId );
}