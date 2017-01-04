package gatech.hi.fhirpit.refapp.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Component
public class ReferralRepo {

	Map<String,RequestInfo> req = new HashMap<String,RequestInfo>();

	public Map<String, RequestInfo> getReq() {
		return req;
	}

	
	public void acceptRequestInfo(RequestInfo req) {

      this.req.put(req.getReferalId(), req);
	}


	
	public RequestInfo getRequestInfo(String id) {
         System.out.println("existing requests >>" + req.size());
		Set<Entry<String, RequestInfo>> entrySet = req.entrySet();
		for (Entry en : entrySet) {
			String key = (String) en.getKey();
			RequestInfo req = (RequestInfo) en.getValue();
			if (StringUtils.equals(key, id)) {
               return req;
			}
		}
         return null;
	}
	
	
	public List<RequestInfo> getAllRequestInfo() {


		System.out.println("existing requests >>" + req.size());
        List<RequestInfo> list = new ArrayList<RequestInfo>(req.values());
        System.out.println("list >>"  + list + " size>>" + list.size());
        
        return list;
		

	}
	

	}
	
	
