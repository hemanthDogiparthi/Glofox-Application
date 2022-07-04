package com.glofox.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@NamedEntityGraph(
	    name = "StudioClass.studioBooking",
	    attributeNodes = {
	            @NamedAttributeNode("studioBooking")
	    }
	)
public class StudioClass {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private int class_id;
	
	private String className;
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	private int capacity;
	private LocalDate startDate;
	private LocalDate endDate;
	private String trainer;
	private String description;
	private String facility;
	
	@Version
    @NotNull
    private Long version;
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@ManyToOne()
	@JoinColumn(name="studio_id")
	@JsonIgnore
	private Studio studio;

	@OneToMany(mappedBy = "studioClass",
			fetch = FetchType.LAZY,
					cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	Set<StudioBooking> studioBooking = new HashSet<>();

	

	public Set<StudioBooking> getStudioBooking() {
		return studioBooking;
	}

	public void setStudioBooking(Set<StudioBooking> studioBooking) {
		this.studioBooking = studioBooking;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getTrainer() {
		return trainer;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public Studio getStudio() {
		return studio;
	}

	public void setStudio(Studio studio) {
		this.studio = studio;
	}

	@Override
	public String toString() {
		return "StudioClass [class_id=" + class_id + ", capacity=" + capacity + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", trainer=" + trainer + ", description=" + description + ", classAddress="
				+ facility + ", studio=" + studio + "]";
	}
}
