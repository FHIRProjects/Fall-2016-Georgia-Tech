package gatech.hi.fhirpit.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

@Component
public class FHIRconfig {

	
//    private static FhirContext ctx = null;
//    private static IGenericClient client = null;
//    private static final String serverBase = "https://secure-api.hspconsortium.org/FHIRPit/open";
    
    
    private FhirContext ctx = null;
    public FhirContext getCtx() {
		return ctx;
	}

	public void setCtx(FhirContext ctx) {
		this.ctx = ctx;
	}

	public IGenericClient getClient() {
		if(client == null)
		{
		initClient();
		}
		
		return client;
	}

	private void initClient() {
        ctx = FhirContext.forDstu2();
        client = ctx.newRestfulGenericClient(serverBase);		
	}

	public void setClient(IGenericClient client) {
		this.client = client;
	}

	private IGenericClient client = null;
	
	
	@Value("${fhir.server}")	
    private String serverBase ;
	
	public FHIRconfig()
	{

	}
	
}
