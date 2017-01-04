package edu.gatech.cs6440.teamdna.dao;

import java.util.List;
import java.util.Map;

import edu.gatech.cs6440.teamdna.model.DoCPDDo;

public interface DoCPDDao {
	public DoCPDDo getById(String id);
	public List<DoCPDDo> findByLicenseNo(String licenseNo);
	public int delete(DoCPDDo docPD) ;
	public int save(DoCPDDo docPD);
	public List<DoCPDDo> find(Map<String, List<String>> queryParams);
}
