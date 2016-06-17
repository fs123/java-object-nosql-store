package ch.ivyteam.orient.fintech;

public class ControllingPerson {
	int share;
	String shareConfirmation; // enum...
	Person person;
	Address address;

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public String getShareConfirmation() {
		return shareConfirmation;
	}

	public void setShareConfirmation(String shareConfirmation) {
		this.shareConfirmation = shareConfirmation;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
