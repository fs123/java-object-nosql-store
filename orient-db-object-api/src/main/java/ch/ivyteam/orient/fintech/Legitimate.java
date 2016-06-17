package ch.ivyteam.orient.fintech;

public class Legitimate {
	private String signatoryRightsRegister; // enum (Sole, Joint, ...)
	private String signatoryRightsBank; // enum SINGLE|ROLE
	private Person person;
	private Contact contact;
	private Address address;

	public String getSignatoryRightsRegister() {
		return signatoryRightsRegister;
	}

	public void setSignatoryRightsRegister(String signatoryRightsRegister) {
		this.signatoryRightsRegister = signatoryRightsRegister;
	}

	public String getSignatoryRightsBank() {
		return signatoryRightsBank;
	}

	public void setSignatoryRightsBank(String signatoryRightsBank) {
		this.signatoryRightsBank = signatoryRightsBank;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
