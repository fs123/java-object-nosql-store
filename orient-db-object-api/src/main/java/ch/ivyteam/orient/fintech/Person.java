package ch.ivyteam.orient.fintech;

import java.util.Date;

public class Person {
	String title;
	String firstName;
	String lastName;
	String nationality;
	String domicileCountry;
	Date birthDate;
	String function;
	String realtionshipToAccountHolder;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDomicileCountry() {
		return domicileCountry;
	}

	public void setDomicileCountry(String domicileCountry) {
		this.domicileCountry = domicileCountry;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getRealtionshipToAccountHolder() {
		return realtionshipToAccountHolder;
	}

	public void setRealtionshipToAccountHolder(String realtionshipToAccountHolder) {
		this.realtionshipToAccountHolder = realtionshipToAccountHolder;
	}

}
