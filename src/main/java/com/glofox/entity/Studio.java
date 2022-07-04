package com.glofox.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@NamedEntityGraph(
	    name = "Studio.studioclass",
	    attributeNodes = {
	            @NamedAttributeNode("studioclass")
	    }
	)
public class Studio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private int studio_id;
	
	@Column(name="name", unique=true)
	private String name;
	
	private String country;
	private String address;
	private String phone;
	private String emailAddress;
	
	@OneToMany(mappedBy = "studio",
				cascade = {CascadeType.PERSIST,CascadeType.MERGE},
	fetch = FetchType.LAZY)
	Set<StudioClass> studioclass = new HashSet<>();
	
	public Set<StudioClass> getStudioclass() {
		return studioclass;
	}
	public void setStudioclass(Set<StudioClass> studioclass) {
		this.studioclass = studioclass;
	}
	public int getStudio_id() {
		return studio_id;
	}
	public void setStudio_id(int studio_id) {
		this.studio_id = studio_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Override
	public String toString() {
		return "Studio [studio_id=" + studio_id + ", name=" + name + ", country=" + country + ", address=" + address
				+ ", phone=" + phone + ", emailAddress=" + emailAddress + "]";
	}
	
	
}
