package model;
import javax.xml.bind.annotation.*;
@XmlAccessorType( XmlAccessType.PROPERTY )
@XmlRootElement( name = "patientRole" )
public class CcdaPatientRole {
    public CcdaPatientRole( ) {
        this.setPatient( new CcdaPatient( ) );
        }
    public CcdaPatient getPatient( ) {
        return this.m_patient;
        }
    public void setPatient( CcdaPatient patient ) {
        this.m_patient = patient;
        }
    private CcdaPatient m_patient;
    }