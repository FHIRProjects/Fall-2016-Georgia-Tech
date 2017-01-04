package model;
import javax.xml.bind.annotation.*;
@XmlAccessorType( XmlAccessType.FIELD )
public class CcdaGiven {
    public CcdaGiven( ) {
        this.setValue( "" );
        this.setQualifier( "" );
        }
    public CcdaGiven( String value, String qualifier ) {
        this.setValue( value );
        this.setQualifier( qualifier );
        }
    public String getValue( ) {
        return this.m_value;
        }
    public void setValue( String value ) {
        this.m_value = value;
        } 
    public String getQualifier( ) {
        return this.m_qualifier;
        }
    public void setQualifier( String qualifier ) {
        this.m_qualifier = qualifier;
        } 
    @XmlValue
    private String m_value;
    @XmlAttribute( name = "qualifier" )
    private String m_qualifier;
    }