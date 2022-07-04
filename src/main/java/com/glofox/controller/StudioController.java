package com.glofox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.glofox.entity.Studio;
import com.glofox.service.StudioService;
import com.glofox.validator.InputValidator;

@RestController
public class StudioController {

	
	@Autowired
	StudioService studioService;
	
	@Autowired
	InputValidator studioInputValidator;

	  @RequestMapping(method = RequestMethod.POST, value="Studio") 
	  public void createStudios(@RequestBody Studio studio) {
			  studioInputValidator.validateInput(studio);
			  studioService.save(studio);
	  }
	  
	  @RequestMapping(method = RequestMethod.GET, value="Studio")  
	  public List<Studio> retrieveAllStudios() { 
		    return studioService.getAllStudios();
		}
}

