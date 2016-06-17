package ch.ivyteam.orient.fintech;

import java.util.Date;

public class AccountHolder {
	String name;
	String type; // enum (cooperative, association, foundation, trust,
						// ...)
	String uidNumber;
	Boolean registered;
	Date dateOfRegistry;
	String nogaCoda;
	String sectorOrMainActivity;
	String purposeOfEntity;
	Number yearOfIncorporation;
	int numberOfEmployees;
	Date dateOfSupply;
	String domicileCountry;
	String domicileCountryControllingPerson;
	String language;
	String contactPerson;
	String bankBranch;

	Address address;
	Correspondence correspondence;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUidNumber() {
		return uidNumber;
	}

	public void setUidNumber(String uidNumber) {
		this.uidNumber = uidNumber;
	}

	public Boolean getRegistered() {
		return registered;
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}

	public Date getDateOfRegistry() {
		return dateOfRegistry;
	}

	public void setDateOfRegistry(Date dateOfRegistry) {
		this.dateOfRegistry = dateOfRegistry;
	}

	public String getNogaCoda() {
		return nogaCoda;
	}

	public void setNogaCoda(String nogaCoda) {
		this.nogaCoda = nogaCoda;
	}

	public String getSectorOrMainActivity() {
		return sectorOrMainActivity;
	}

	public void setSectorOrMainActivity(String sectorOrMainActivity) {
		this.sectorOrMainActivity = sectorOrMainActivity;
	}

	public String getPurposeOfEntity() {
		return purposeOfEntity;
	}

	public void setPurposeOfEntity(String purposeOfEntity) {
		this.purposeOfEntity = purposeOfEntity;
	}

	public Number getYearOfIncorporation() {
		return yearOfIncorporation;
	}

	public void setYearOfIncorporation(Number yearOfIncorporation) {
		this.yearOfIncorporation = yearOfIncorporation;
	}

	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}

	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	public Date getDateOfSupply() {
		return dateOfSupply;
	}

	public void setDateOfSupply(Date dateOfSupply) {
		this.dateOfSupply = dateOfSupply;
	}

	public String getDomicileCountry() {
		return domicileCountry;
	}

	public void setDomicileCountry(String domicileCountry) {
		this.domicileCountry = domicileCountry;
	}

	public String getDomicileCountryControllingPerson() {
		return domicileCountryControllingPerson;
	}

	public void setDomicileCountryControllingPerson(String domicileCountryControllingPerson) {
		this.domicileCountryControllingPerson = domicileCountryControllingPerson;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Correspondence getCorrespondence() {
		return correspondence;
	}

	public void setCorrespondence(Correspondence correspondence) {
		this.correspondence = correspondence;
	}

}
