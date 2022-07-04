package com.glofox.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glofox.entity.Studio;
import com.glofox.entity.StudioClass;
import com.glofox.repository.StudioRepository;

@Service
public class StudioService {

	  	@Autowired
	    StudioRepository studioRepository;

	    public void save(Studio studio) {
	    	for (StudioClass studioClass : studio.getStudioclass()) {
	    		studioClass.setStudio(studio);
	    	}
	    	studioRepository.save(studio);
	    }
	    
	    public List<Studio> getAllStudios() {
	        return (List<Studio>) studioRepository.findAll();
	    }

		public Optional<Studio> getStudioByName(String studioName) {
			// TODO Auto-generated method stub
			return studioRepository.findByName(studioName);
		}
}
