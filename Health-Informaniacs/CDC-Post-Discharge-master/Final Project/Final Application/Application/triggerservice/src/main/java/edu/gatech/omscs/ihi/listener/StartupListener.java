package edu.gatech.omscs.ihi.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import edu.gatech.omscs.ihi.service.TriggerService;

@Component
public class StartupListener implements ApplicationListener< ContextRefreshedEvent > 
{
	@Autowired
	private TriggerService triggerService;
	
	@Value( "${initialFetchDays}" )
	private int initialFetchDays;
	
	@Override
	public void onApplicationEvent( final ContextRefreshedEvent event )
	{
		this.triggerService.execute( initialFetchDays );
	}
}