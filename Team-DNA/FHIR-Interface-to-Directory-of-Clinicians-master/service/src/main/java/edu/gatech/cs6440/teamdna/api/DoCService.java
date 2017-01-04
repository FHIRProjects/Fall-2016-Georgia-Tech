package edu.gatech.cs6440.teamdna.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.gatech.cs6440.teamdna.business.DoCManager;
import edu.gatech.cs6440.teamdna.model.DoCPDDo;

import java.io.IOException;
import java.util.List;

@Component
@Path("licensure")
public class DoCService {
	
	@Autowired
	private DoCManager docManager;
	
	public void setDocManager(DoCManager docManager) {
		this.docManager = docManager;
	}

	@GET
	@Path("byLicenseNo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByLicenseNo(@QueryParam("licenseNo") String licenseNo){
		List<DoCPDDo> licenses = docManager.searchByLicenseNo(licenseNo);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(licenses);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(json).build();
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNew(DoCPDDo doCPD){
		return Response.ok(docManager.addNew(doCPD)).build();
	}
}
