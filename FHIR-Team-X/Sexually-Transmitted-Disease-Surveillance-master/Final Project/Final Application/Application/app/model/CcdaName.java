package model;
import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement( name = "name" )
public class CcdaName {
    public CcdaName( ) {
        this.setGiven( new ArrayList<CcdaGiven>() );
        this.setFamily( "" );
        }
    public List<CcdaGiven> getGiven( ) {
        return this.m_given;
        }
    public void setGiven( List<CcdaGiven> given ) {
        this.m_given = given;
        } 
    public void addGiven( String value, String qualifier ) {
        this.m_given.add( new CcdaGiven( value, qualifier ) );
        }
    public String getFamily( ) {
        return this.m_family;
        }
    public void setFamily( String family ) {
        this.m_family = family;
        } 
    @XmlElement( name = "given" )
    private List<CcdaGiven> m_given;
    @XmlElement( name = "family" )
    private String m_family;
    }