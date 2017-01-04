package edu.gatech.omscs.ihi.util;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils 
{
	private static ObjectMapper objectMapper = new ObjectMapper();
	
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
	
	public static JsonNode convertKeyValueToJson( String key, String value )
	{
		Map< String, String > map = new HashMap< String, String >(); 
		
		map.put( key, value );
		
		return convertBeanToJsonNode( map );
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
	
	
	public static Object convertJsonToBean( String jsonValue, Class<?> valueType ) 
	{
		Reader strReader = null;
		try 
		{
			strReader = new StringReader( jsonValue );
			return objectMapper.readValue( strReader, valueType );
		} 
		catch ( Exception exp ) 
		{
			exp.printStackTrace();
		}
		finally 
		{
			try 
			{
				if ( strReader != null ) 
				{
					strReader.close();
				}
			} 
			catch ( Exception e ) 
			{
				e.printStackTrace();
			}
		}
		return null;
	}
}