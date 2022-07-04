package com.glofox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.glofox.entity.Studio;
import com.glofox.entity.StudioClass;

public interface StudioClassRepository extends CrudRepository<StudioClass, Integer> {
	
	public List<StudioClass> findByStudio(Studio studio);
	
	public StudioClass findByclassName(String className);
}
