package edu.gatech.omscs.ihi.service;

import org.springframework.stereotype.Service;

@Service( value = "TriggerService" )
public interface TriggerService 
{
	void timerExecute();
	
	void execute( int days );
}