package ch.ivyteam.incubator.mongodb.hibernate.ogm.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Dog {
	// @Id //@GeneratedValue(strategy = GenerationType.TABLE, generator = "dog")
	// @TableGenerator(
	// name = "dog",
	// table = "sequences",
	// pkColumnName = "key",
	// pkColumnValue = "dog",
	// valueColumnName = "seed"
	// )
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	@ManyToOne(cascade = CascadeType.ALL)
	public Breed getBreed() {
		return breed;
	}

	public void setBreed(Breed breed) {
		this.breed = breed;
	}

	private Breed breed;

	@Override
	public String toString() {
		return "ID: " + id + " name: " + name + " breed: " + (breed == null ? "null" : breed.getName());
	}
}
