package edu.gatech.cs6440.teamdna.business;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import edu.gatech.cs6440.teamdna.model.DoCPDDo;

@XmlRootElement
public class DoCPD {
	private DoCPDDo doCPDDo;

	public DoCPD(DoCPDDo doCPDDo) {
		this.doCPDDo = doCPDDo;
	}
	
	public DoCPD() {
		this(new DoCPDDo());
	}
	
	public DoCPDDo getDoCPDDo() {
		return doCPDDo;
	}

	public Integer getId() {
		return doCPDDo.getId();
	}

	public String getDocPdId() {
		return doCPDDo.getDocPdId();
	}

	public String getLicenseNo() {
		return doCPDDo.getLicenseNo();
	}

	public String getProfessionName() {
		return doCPDDo.getProfessionName();
	}

	public String getLicenseName() {
		return doCPDDo.getLicenseName();
	}

	public String getFirstName() {
		return doCPDDo.getFirstName();
	}

	public String getLastName() {
		return doCPDDo.getLastName();
	}

	public String getSortName() {
		return doCPDDo.getSortName();
	}

	public String getFullName() {
		return doCPDDo.getFullName();
	}

	public String getAka() {
		return doCPDDo.getAka();
	}

	public String getNameSuffix() {
		return doCPDDo.getNameSuffix();
	}

	public Date getDateOfBirth() {
		return ConversionUtils.toDate(doCPDDo.getDateOfBirth());
	}

	public String getSsn() {
		return doCPDDo.getSsn();
	}

	public Date getIssueDate() {
		return ConversionUtils.toDate(doCPDDo.getIssueDate());
	}

	public Date getExpirationDate() {
		return ConversionUtils.toDate(doCPDDo.getExpirationDate());
	}

	public String getLicStatus() {
		return getLicStatus();
	}

	public Date getDateThisStatus() {
		return ConversionUtils.toDate(doCPDDo.getDateThisStatus());
	}

	public String getAddrLine1() {
		return doCPDDo.getAddrLine1();
	}

	public String getAddrLine2() {
		return doCPDDo.getAddrLine2();
	}

	public String getAddrCity() {
		return doCPDDo.getAddrCity();
	}

	public String getAddrState() {
		return doCPDDo.getAddrState();
	}

	public String getAddrZipcode() {
		return doCPDDo.getAddrZipcode();
	}

	public String getAddrCounty() {
		return doCPDDo.getAddrCounty();
	}

	public String getAddrPhone() {
		return doCPDDo.getAddrPhone();
	}

	public String getAddrEmail() {
		return doCPDDo.getAddrEmail();
	}

	public Date getDateDeceased() {
		return ConversionUtils.toDate(doCPDDo.getDateDeceased());
	}

	public String getLicenseName2() {
		return doCPDDo.getLicenseName2();
	}

	public String getDiscipline() {
		return doCPDDo.getDiscipline();
	}

	public String getFederalId() {
		return doCPDDo.getFederalId();
	}

	public void setDocPdId(String docPdId) {
		doCPDDo.setDocPdId(docPdId);
	}

	public void setLicenseNo(String licenseNo) {
		doCPDDo.setLicenseNo(licenseNo);
	}

	public void setProfessionName(String professionName) {
		doCPDDo.setProfessionName(professionName);
	}

	public void setLicenseName(String licenseName) {
		doCPDDo.setLicenseName(licenseName);
	}

	public void setFirstName(String firstName) {
		doCPDDo.setFirstName(firstName);
	}

	public void setLastName(String lastName) {
		doCPDDo.setLastName(lastName);
	}

	public void setSortName(String sortName) {
		doCPDDo.setSortName(sortName);
	}

	public void setFullName(String fullName) {
		doCPDDo.setFullName(fullName);
	}

	public void setAka(String aka) {
		doCPDDo.setAka(aka);
	}

	public void setNameSuffix(String nameSuffix) {
		doCPDDo.setNameSuffix(nameSuffix);
	}

	public void setDateOfBirth(Date dateOfBirth) {
		doCPDDo.setDateOfBirth(ConversionUtils.toString(dateOfBirth));
	}

	public void setSsn(String ssn) {
		doCPDDo.setSsn(ssn);
	}

	public void setIssueDate(Date issueDate) {
		doCPDDo.setIssueDate(ConversionUtils.toString(issueDate));
	}

	public void setExpirationDate(Date expirationDate) {
		doCPDDo.setExpirationDate(ConversionUtils.toString(expirationDate));
	}

	public void setLicStatus(String licStatus) {
		doCPDDo.setLicStatus(licStatus);
	}

	public void setDateThisStatus(Date dateThisStatus) {
		doCPDDo.setDateThisStatus(ConversionUtils.toString(dateThisStatus));
	}

	public void setAddrLine1(String addrLine1) {
		doCPDDo.setAddrLine1(addrLine1);
	}

	public void setAddrLine2(String addrLine2) {
		doCPDDo.setAddrLine2(addrLine2);
	}

	public void setAddrCity(String addrCity) {
		doCPDDo.setAddrCity(addrCity);
	}

	public void setAddrState(String addrState) {
		doCPDDo.setAddrState(addrState);
	}

	public void setAddrZipcode(String addrZipcode) {
		doCPDDo.setAddrZipcode(addrZipcode);
	}

	public void setAddrCounty(String addrCounty) {
		doCPDDo.setAddrCounty(addrCounty);
	}

	public void setAddrPhone(String addrPhone) {
		doCPDDo.setAddrPhone(addrPhone);
	}

	public void setAddrEmail(String addrEmail) {
		doCPDDo.setAddrEmail(addrEmail);
	}

	public void setDateDeceased(Date dateDeceased) {
		doCPDDo.setDateDeceased(ConversionUtils.toString(dateDeceased));
	}

	public void setLicenseName2(String licenseName2) {
		doCPDDo.setLicenseName2(licenseName2);
	}

	public void setDiscipline(String discipline) {
		doCPDDo.setDiscipline(discipline);
	}

	public void setFederalId(String federalId) {
		doCPDDo.setFederalId(federalId);
	}
}
