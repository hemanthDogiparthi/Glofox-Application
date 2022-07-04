package com.glofox.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glofox.entity.Studio;
import com.glofox.entity.StudioBooking;
import com.glofox.entity.StudioClass;
import com.glofox.repository.StudioClassRepository;

@Service
public class StudioClassService {

	  	@Autowired
	    StudioClassRepository classRepository;

	    public void save(StudioClass studioClass) {
	    	classRepository.save(studioClass);
	    }
	    
	    public List<StudioClass> getAllClasses(Studio studio) {
	    	return  classRepository.findByStudio(studio);
	    }
	    

	    public Optional<StudioClass> getClassById(int classId) {
	    	return classRepository.findById(classId);
	    }

		public void deleteClassbyId(int classId) {
			// TODO Auto-generated method stub
			 classRepository.deleteById(classId);
		}

		public StudioClass findClassbyName(String className) {
			// TODO Auto-generated method stub
			return classRepository.findByclassName(className);
		}
}
