package com.glofox.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glofox.entity.Studio;
import com.glofox.entity.StudioClass;
import com.glofox.exceptions.MissingDataException;
import com.glofox.service.StudioClassService;
import com.glofox.service.StudioService;
import com.glofox.validator.InputValidator;

@RestController
public class StudioClassesController {

	@Autowired
	StudioClassService classService;

	@Autowired
	StudioService studioService;
	
	@Autowired
	InputValidator studioClassInputValidator;

	@RequestMapping(method = RequestMethod.POST, value = "{studioName}/Classes")
	public void createClasses(@RequestBody StudioClass studioClass, @PathVariable String studioName) {
			studioClassInputValidator.validateInput(studioClass);
			Studio studio = studioService.getStudioByName(studioName)
					.orElseThrow(() -> new MissingDataException("No Studio present with the name ".concat(studioName)));
			studioClass.setStudio(studio);
			studio.getStudioclass().add(studioClass);
			classService.save(studioClass);	
	}

	@RequestMapping(method = RequestMethod.GET, value ="{studioName}/Classes")
	public List<StudioClass> retrieveAllClasses(@PathVariable String studioName) {
			Studio studio = studioService.getStudioByName(studioName)
					.orElseThrow(() -> new MissingDataException("No Studio present with the name ".concat(studioName)));
			
			return classService.getAllClasses(studio);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "{classId}/Classes")
	public void deleteClassById( @PathVariable int classId){
		classService.deleteClassbyId(classId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "ClassName/{className}/Classes")
	public StudioClass findClassbyName( @PathVariable String className){
		return classService.findClassbyName(className);
}
