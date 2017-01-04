package edu.gatech.cs6440.teamdna.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement
public class DoCPDDo {
	private Integer id;
	private String docPdId;
	private String licenseNo;
	private String professionName;
	private String licenseName;
	private String firstName;
	private String lastName;
	private String sortName;
	private String fullName;
	private String aka;
	private String nameSuffix;
	private String dateOfBirth;
	private String ssn;
	private String issueDate;
	private String expirationDate;
	private String licStatus;
	private String dateThisStatus;
	private String addrLine1;
	private String addrLine2;
	private String addrCity;
	private String addrState;
	private String addrZipcode;
	private String addrCounty;
	private String addrPhone;
	private String addrEmail;
	private String dateDeceased;
	private String licenseName2;
	private String discipline;
	private String federalId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDocPdId() {
		return docPdId;
	}
	public void setDocPdId(String docPdId) {
		this.docPdId = docPdId;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getProfessionName() {
		return professionName;
	}
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	public String getLicenseName() {
		return licenseName;
	}
	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAka() {
		return aka;
	}
	public void setAka(String aka) {
		this.aka = aka;
	}
	public String getNameSuffix() {
		return nameSuffix;
	}
	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getLicStatus() {
		return licStatus;
	}
	public void setLicStatus(String licStatus) {
		this.licStatus = licStatus;
	}
	public String getDateThisStatus() {
		return dateThisStatus;
	}
	public void setDateThisStatus(String dateThisStatus) {
		this.dateThisStatus = dateThisStatus;
	}
	public String getAddrLine1() {
		return addrLine1;
	}
	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}
	public String getAddrLine2() {
		return addrLine2;
	}
	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}
	public String getAddrCity() {
		return addrCity;
	}
	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}
	public String getAddrState() {
		return addrState;
	}
	public void setAddrState(String addrState) {
		this.addrState = addrState;
	}
	public String getAddrZipcode() {
		return addrZipcode;
	}
	public void setAddrZipcode(String addrZipcode) {
		this.addrZipcode = addrZipcode;
	}
	public String getAddrCounty() {
		return addrCounty;
	}
	public void setAddrCounty(String addrCounty) {
		this.addrCounty = addrCounty;
	}
	public String getAddrPhone() {
		return addrPhone;
	}
	public void setAddrPhone(String addrPhone) {
		this.addrPhone = addrPhone;
	}
	public String getAddrEmail() {
		return addrEmail;
	}
	public void setAddrEmail(String addrEmail) {
		this.addrEmail = addrEmail;
	}
	public String getDateDeceased() {
		return dateDeceased;
	}
	public void setDateDeceased(String dateDeceased) {
		this.dateDeceased = dateDeceased;
	}
	public String getLicenseName2() {
		return licenseName2;
	}
	public void setLicenseName2(String licenseName2) {
		this.licenseName2 = licenseName2;
	}
	public String getDiscipline() {
		return discipline;
	}
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	public String getFederalId() {
		return federalId;
	}
	public void setFederalId(String federalId) {
		this.federalId = federalId;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
}
