package edu.gatech.omscs.ihi.listener;


import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import edu.gatech.omscs.ihi.domain.Patient;
import edu.gatech.omscs.ihi.domain.Destination;
import edu.gatech.omscs.ihi.domain.StrokeCode;
import edu.gatech.omscs.ihi.domain.SurveyAdmin;
import edu.gatech.omscs.ihi.domain.id.PatientId;
import edu.gatech.omscs.ihi.repository.DestinationRepository;
import edu.gatech.omscs.ihi.repository.PatientRepository;
import edu.gatech.omscs.ihi.repository.StrokeCodeRepository;
import edu.gatech.omscs.ihi.repository.SurveyAdminRepository;

@Component
public class StartupListener implements ApplicationListener< ContextRefreshedEvent > 
{	
	@Autowired
	private DestinationRepository destinationRepository;
	
	@Autowired
	private SurveyAdminRepository surveyAdminRepository;
	
	@Autowired
	private StrokeCodeRepository strokeCodeRepository;
	
	@Autowired
	private PatientRepository patientRepositry;
	

	
	@SuppressWarnings("deprecation")
	@Override
	public void onApplicationEvent( final ContextRefreshedEvent event )
	{
		this.destinationRepository.deleteAll();
		this.surveyAdminRepository.deleteAll();
		this.strokeCodeRepository.deleteAll();
		
		// Create some survey admins and associate them with destinations Hospital
		this.surveyAdminRepository.save( new SurveyAdmin( 1, "Nurse", "Jane", "nurse_jane", "password" ) );
		this.destinationRepository.save( new Destination( 1, "City Regional Hospital", 1 ) );
		
		// Acute care
		this.surveyAdminRepository.save( new SurveyAdmin( 2, "Nurse Practioner", "John", "np_john", "password" ) );
		this.destinationRepository.save( new Destination( 2, "Citizens Acute Care Center", 2 ) );
		
		// Long term care
		this.surveyAdminRepository.save( new SurveyAdmin( 3, "Long Term Nurse", "Charles", "ltm_charles", "password" ) );
		this.destinationRepository.save( new Destination( 3, "Century Assissted Living and Long Term Car", 3) );
		
		// Acute rehab
		this.surveyAdminRepository.save( new SurveyAdmin( 4, "Therapist", "Jean", "therapist_jean", "password" ) );
		this.destinationRepository.save( new Destination( 4, "Strength Physical Therapy Center", 4 ) );
		
		// Skilled nursing facility
		this.surveyAdminRepository.save( new SurveyAdmin( 5, "Facility Nurse", "Logan", "fn_logan", "password" ) );
		this.destinationRepository.save( new Destination( 5, "National Health Facility", 5 ) );
		
		// Home 
		this.surveyAdminRepository.save( new SurveyAdmin( 0, "Home Nurse", "Joe", "hn_joe", "password" ) );
		this.destinationRepository.save( new Destination( 0, "Patient residence", 0 ) );
		
		//Dummy patients
		this.patientRepositry.save(new Patient(new PatientId("99900004","1039","1"),"Steve", "Waugh",new Date(2016-1900,10,20),new Date(2016-1900,10,13), "{}","","{}","One, Two, Three" ));
		this.patientRepositry.save(new Patient(new PatientId("99900005","1040","2"),"Mark", "Waugh",new Date(2016-1900,10,21),new Date(2016-1900,10,14), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900006","1041","3"),"Graham", "Thorpe",new Date(2016-1900,10,22),new Date(2016-1900,10,15), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900007","1042","4"),"Nasser", "Hussain",new Date(2016-1900,10,23),new Date(2016-1900,10,16), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900008","1043","5"),"Pat", "Symcox",new Date(2016-1900,10,24),new Date(2016-1900,10,17), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900009","1044","1"),"Priyanka", "Chopra",new Date(2016-1900,10,25),new Date(2016-1900,10,18), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900010","1045","1"),"Kristen", "Stewart",new Date(2016-1900,10,26),new Date(2016-1900,10,19), "{}","","{}","Four,Five,Six" ));
		this.patientRepositry.save(new Patient(new PatientId("99900011","1046","2"),"Selena", "Gomez",new Date(2016-1900,10,27),new Date(2016-1900,10,20), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900012","1047","3"),"Emma", "Watson",new Date(2016-1900,10,28),new Date(2016-1900,10,21), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900013","1048","4"),"Deepika", "Padukone",new Date(2016-1900,10,29),new Date(2016-1900,10,22), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900014","1049","5"),"Donald", "Trump",new Date(2016-1900,10,30),new Date(2016-1900,10,23), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900015","1050","1"),"Barack", "Obama",new Date(2016-1900,9,20),new Date(2016-1900,9,13), "{}","12344555","{}","Seven,Eight,Nine" ));
		this.patientRepositry.save(new Patient(new PatientId("99900016","1051","2"),"George", "Bush",new Date(2016-1900,9,21),new Date(2016-1900,9,14), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900017","1052","3"),"Ronald", "Reagan",new Date(2016-1900,9,22),new Date(2016-1900,9,15), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900018","1053","4"),"Bill", "Clinton",new Date(2016-1900,9,23),new Date(2016-1900,9,16), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900019","1054","5"),"Jonny", "Quest",new Date(2016-1900,9,24),new Date(2016-1900,9,17), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900020","1055","1"),"Fred", "Flinstone",new Date(2016-1900,9,25),new Date(2016-1900,9,18), "{}" ));
		this.patientRepositry.save(new Patient(new PatientId("99900021","1056","2"),"Dexter", "McPherson",new Date(2016-1900,9,26),new Date(2016-1900,9,19), "{}" ));
		
		// Hemorrhagic and Ischemic Stroke (The Joint Commission)
		this.strokeCodeRepository.save( new StrokeCode( "I6000" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6001" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6002" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6010" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6011" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6012" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I602" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6030" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6031" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6032" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I604" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6050" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6051" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6052" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I606" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I607" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I608" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I609" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I610" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I611" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I612" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I613" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I614" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I615" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I616" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I618" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I619" ) );
		
		this.strokeCodeRepository.save( new StrokeCode( "I6300" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63011" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63012" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63013" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63019" ) ) ;
		this.strokeCodeRepository.save( new StrokeCode( "I6302" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63031" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63032" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63033" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63039" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6309" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6310" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63111" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63112" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63113" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63119" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6312" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63131" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63132" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63133" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63139" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6319" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6320" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63211" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63212" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63213" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63219" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63231" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63232" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63233" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63239" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6329" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6330" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63311" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63312" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63313" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63319" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63321" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63322" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63323" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63329" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63331" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63332" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63333" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63339" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63341" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63342" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63343" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63349" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6339" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6340" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63411" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63412" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63413" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63419" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63421" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63422" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63423" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63429" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63431" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63432" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63433" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63439" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63441" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63442" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63443" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63449" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6350" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63511" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63512" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63513" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63519" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63521" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63522" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63523" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63529" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63531" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63532" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63533" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63539" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63541" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63542" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63543" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I63549" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I6359" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I636" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I638" ) );
		this.strokeCodeRepository.save( new StrokeCode( "I639" ) );
		
		// Transient Ischemic Attack
		this.strokeCodeRepository.save( new StrokeCode( "G45.0" ) );
		this.strokeCodeRepository.save( new StrokeCode( "G45.1" ) );
		this.strokeCodeRepository.save( new StrokeCode( "G45.2" ) );
		this.strokeCodeRepository.save( new StrokeCode( "G45.8" ) );
		this.strokeCodeRepository.save( new StrokeCode( "G45.9" ) );
		this.strokeCodeRepository.save( new StrokeCode( "G46.0" ) );
		this.strokeCodeRepository.save( new StrokeCode( "G46.1" ) );
		this.strokeCodeRepository.save( new StrokeCode( "G46.2" ) );
		
		// Pregnancy
		this.strokeCodeRepository.save( new StrokeCode( "O99.411" ) );
		this.strokeCodeRepository.save( new StrokeCode( "O99.412" ) );
		this.strokeCodeRepository.save( new StrokeCode( "O99.413" ) );
		this.strokeCodeRepository.save( new StrokeCode( "O99.419" ) );
	}
}