package edu.gatech.cs6440.teamdna.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.gatech.cs6440.teamdna.business.BundleDirector;
import edu.gatech.cs6440.teamdna.business.DoCManager;
import edu.gatech.cs6440.teamdna.business.PractitionerDirector;
import edu.gatech.cs6440.teamdna.fhir.Bundle;
import edu.gatech.cs6440.teamdna.fhir.Practitioner;
import edu.gatech.cs6440.teamdna.model.DoCPDDo;

@Component
@Path("practitioner")
public class PractitionerService {
		@Autowired
		private DoCManager docManager;
		
		@Context 
		UriInfo uriInfo;
		
		public void setDocManager(DoCManager docManager) {
			this.docManager = docManager;
		}
		

		@GET
		@Path("id/{id}")
		@Produces(MediaType.APPLICATION_XML)
		public Response findById(@PathParam("id") java.lang.String id){
			Practitioner practitioner = transform(docManager.searchById(id));
			return Response.ok(practitioner).build();
		}

		@GET
		@Path("byLicenseNo")
		@Produces(MediaType.APPLICATION_JSON)
		public Response findByLicenseNo(@QueryParam("licenseNo") java.lang.String licenseNo){
			Bundle practitioners = transform(docManager.searchByLicenseNo(licenseNo));
			return Response.ok(practitioners).build();
		}
		
		@GET
		@Path("xml/lookup")
		@Produces(MediaType.APPLICATION_XML)
		public Response lookupXML(){
			MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
			Bundle practitioners = lookup(queryParams);

			return Response.ok(practitioners).build();
		}
		
		@GET
		@Path("json/lookup")
		@Produces(MediaType.APPLICATION_JSON)
		public Response lookupJSON(){
			MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

			return Response.ok(lookup(queryParams)).build();
		}
		
		private Bundle lookup(MultivaluedMap<String, String> queryParams){
			return transform(docManager.search(queryParams));
		}

		private Practitioner transform(DoCPDDo docPD) {
			return PractitionerDirector.build(docPD);
		}
		
		private Bundle transform(List<DoCPDDo> docPDs) {
			String protocol = uriInfo.getBaseUri().getScheme();
			String hostname = uriInfo.getBaseUri().getHost();
			int port = uriInfo.getBaseUri().getPort();
			return BundleDirector.build(docPDs, protocol, hostname, Integer.toString(port), "fhir/practitioner/id");
		}
}
