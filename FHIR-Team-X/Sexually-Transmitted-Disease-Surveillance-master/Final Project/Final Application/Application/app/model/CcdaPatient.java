package model;
import javax.xml.bind.annotation.*;
@XmlAccessorType( XmlAccessType.PROPERTY )
@XmlRootElement( name = "patient" )
public class CcdaPatient {
    public CcdaPatient( ) {
        this.setName( new CcdaName( ) );
        }
    public CcdaName getName( ) {
        return this.m_name;
        }
    public void setName( CcdaName name ) {
        this.m_name = name;
        }
    private CcdaName m_name;
    }