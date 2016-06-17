package ch.ivyteam.orient.store;

public class Address {

	private String name;
	private City city;
	private String street;

	public Address()
	{

	}

	public Address(String name, City city, String street) {
		this.setName(name);
		this.setCity(city);
		this.setStreet(street);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

}
