package ch.ivyteam.orient.store;

public class City {

	private String name;
	private Country country;

	public City() {

	}

	public City(String name, Country country) {
		this.setName(name);
		this.setCountry(country);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}
