package model;
import javax.xml.bind.annotation.*;
@XmlAccessorType( XmlAccessType.PROPERTY )
@XmlRootElement( name = "recordTarget" )
public class CcdaRecordTarget {
    public CcdaRecordTarget( ) {
        this.setPatientRole( new CcdaPatientRole( ) );
        }
    public CcdaPatientRole getPatientRole( ) {
        return this.m_patientRole;
        }
    public void setPatientRole( CcdaPatientRole patientRole ) {
        this.m_patientRole = patientRole;
        }
    private CcdaPatientRole m_patientRole;
    }