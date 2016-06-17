package ch.ivyteam.orient.store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Account {

	private String name;
	private String firstname;
	private String surname;
	private Date birthdate;
	private List<Address> adresses = new ArrayList<>();

	public Account() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSurname() {
		return surname;
	}

	public List<Address> getAddresses() {
		return adresses;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthday) {
		this.birthdate = birthday;
	}


}
