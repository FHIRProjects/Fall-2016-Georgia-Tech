package edu.gatech.omscs.ihi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TriggerServiceApplication
{
	public static void main( String[] args ) 
	{
		SpringApplication.run( TriggerServiceApplication.class, args );
	}
}