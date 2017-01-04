package edu.gatech.omscs.ihi.util;

import java.io.StringWriter;
import java.io.Writer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils 
{
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static ObjectMapper getObjectMapper()
	{
		return objectMapper;
	}
	
	public static JsonNode converStringToJsonNode( String string )
	{
		try
		{
			return objectMapper.readTree( string );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		
		return null;
	}
	
	public static String convertBeanToJsonString( Object bean )
	{
		Writer stringWriter = null;
		try
		{
			stringWriter = new StringWriter();
			objectMapper.writeValue( stringWriter, bean );
			return stringWriter.toString();
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		finally
		{
			try
			{
				if ( stringWriter != null )
				{
					stringWriter.close();
					stringWriter = null;
				}
			}
			catch ( Exception exp )
			{
				exp.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static JsonNode convertBeanToJsonNode( Object bean )
	{
		try
		{
			return objectMapper.readTree( convertBeanToJsonString( bean ) );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		
		return null;
	}
}