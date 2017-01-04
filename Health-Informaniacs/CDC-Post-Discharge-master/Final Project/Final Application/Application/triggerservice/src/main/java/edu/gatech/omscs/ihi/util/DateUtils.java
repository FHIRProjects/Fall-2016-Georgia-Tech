package edu.gatech.omscs.ihi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class DateUtils 
{
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
	
	public static String getPastDateString( int days ) 
	{
        Calendar cal = Calendar.getInstance();
        
        cal.add( Calendar.DATE, ( -1 * days ) );
        
        return dateFormat.format( cal.getTime() );
    }
}