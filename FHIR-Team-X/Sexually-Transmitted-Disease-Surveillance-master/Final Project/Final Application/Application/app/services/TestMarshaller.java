package services;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import model.CcdaRecordTarget;
import org.w3c.dom.Document;

public class TestMarshaller
{
    public TestMarshaller( )
    {
         this.rt = new CcdaRecordTarget( );
         this.rt.getPatientRole().getPatient().getName().addGiven( "First", null );
         this.rt.getPatientRole().getPatient().getName().addGiven( "Middle", "IN" );
    }
    
    public void testit( Document doc )
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance( CcdaRecordTarget.class );
            Marshaller  marshaller = context.createMarshaller( );
            Unmarshaller unmarshaller = context.createUnmarshaller( );
            
            //this.rt = (CcdaRecordTarget) JAXBIntrospector.getValue(unmarshaller.unmarshal( doc, CcdaRecordTarget.class ));

            // use linefeeds and indentation in the XML output
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            marshaller.marshal( this.rt, System.out );    


        }
        catch ( JAXBException e )
        {
            System.out.println( e.getMessage( ) );
        }
    } // public void testit( )

    public CcdaRecordTarget rt;
} // public class TestMarshaller