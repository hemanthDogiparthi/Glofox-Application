package com.glofox.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.glofox.entity.Studio;


public interface StudioRepository extends CrudRepository<Studio, Integer> {
	
	public Optional<Studio> findByName(String name);
	}

