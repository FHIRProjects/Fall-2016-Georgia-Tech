package edu.gatech.cs6440.teamdna.business;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.gatech.cs6440.teamdna.dao.DoCPDDao;
import edu.gatech.cs6440.teamdna.model.DoCPDDo;

@Component
public class DoCManager {

	@Autowired
	private DoCPDDao docPDDao;
	
	public DoCPDDo searchById(String id){
		return docPDDao.getById(id);
	}
	
	public List<DoCPDDo> searchByLicenseNo(String licenseNo){
//		List<DoCPD> result = new ArrayList<DoCPD>();
//		
//		List<DoCPDDo> dataObject = docPDDao.findByLicenseNo(licenseNo);
//		for (DoCPDDo doCPDDo : dataObject) {
//			result.add(new DoCPD(doCPDDo));
//		}
//		
//		return result;
		
		return docPDDao.findByLicenseNo(licenseNo);
	}
	
	public boolean addNew(DoCPDDo docPD){
		if(docPD != null){
			return docPDDao.save(docPD) > 0;
		}
		
		return false;
	}
	
	public List<DoCPDDo> search(Map<String, List<String>> queryParams) {
		return docPDDao.find(queryParams);
	}
}
