package com.glofox.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.glofox.entity.Studio;
import com.glofox.entity.StudioBooking;
import com.glofox.entity.StudioClass;
import com.glofox.exceptions.ConcurrencyException;
import com.glofox.exceptions.MissingDataException;
import com.glofox.exceptions.ServiceException;
import com.glofox.service.StudioBookingService;
import com.glofox.service.StudioClassService;
import com.glofox.service.StudioService;
import com.glofox.validator.InputValidator;

@RestController
public class StudioBookingsController {

	@Autowired
	StudioBookingService studioBookingService;

	@Autowired
	StudioClassService studioClassService;

	@Autowired
	StudioService studioService;

	@Autowired
	InputValidator studioBookingInputValidator;

	/**
	 * @param request
	 * @param studioBooking
	 * @param studioName
	 * @param className
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{studioName}/{className}/Bookings")
	public ResponseEntity<?> createBookings(WebRequest request, @RequestBody StudioBooking studioBooking,
			@PathVariable String studioName, @PathVariable String className) {

		studioBookingInputValidator.validateInput(studioBooking);
		Studio studio = studioService.getStudioByName(studioName)
				.orElseThrow(() -> new MissingDataException("No Studio present with the name ".concat(studioName)));

		StudioClass studioClass = studio.getStudioclass().stream()
				.filter(s -> s.getClassName().equalsIgnoreCase(className)).findAny()
				.orElseThrow(() -> new MissingDataException("No Classes present with the name ".concat(className)
						.concat(" for studio ").concat(studioName)));

		studioBookingInputValidator.validateBookingDate(studioClass, studioBooking);
		
		studioBookingInputValidator.validateConcurrentRequests(request, studioClass.getVersion(), studioBooking);

		studioBookingInputValidator.validateCapacity(studioClass, studioBooking, studioName);

		studioBooking.setStudioClass(studioClass);
		studioClass.getStudioBooking().add(studioBooking);
		
		try {
			studioBookingService.save(studioBooking);
	    } catch (OptimisticLockingFailureException ex){
	    	throwConcurrentException(studioClass.getVersion());
		}
		
		return ResponseEntity.ok().eTag("\"" + studioClass.getVersion() + "\"").build();
	}

	
	
	private void throwConcurrentException(Long version) {
		// TODO Auto-generated method stub
		throw new ConcurrencyException("The Bookings version changed Use this Updated parameter  in the resource Header  " + version);
	}



	/**
	 * @param studioName
	 * @param className
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{studioName}/{className}/Bookings")
	public ResponseEntity<Set<StudioBooking>> retrieveAllClasses(@PathVariable String studioName,
			@PathVariable String className) {
		Studio studio = studioService.getStudioByName(studioName)
				.orElseThrow(() -> new MissingDataException("No Studio present with the name ".concat(studioName)));

		StudioClass studioClass = studio.getStudioclass().stream()
				.filter(s -> s.getClassName().equalsIgnoreCase(className)).findAny()
				.orElseThrow(() -> new MissingDataException("No Classes present with the name ".concat(className)
						.concat(" for studio ").concat(studioName)));

		Set<StudioBooking> bookingList = studioClass.getStudioBooking();

		return ResponseEntity.ok().eTag("\"" + studioClass.getVersion() + "\"").body(bookingList);
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE, value = "{bookingId}/Bookings")
	public void deleteBookings( @PathVariable int bookingId){
		studioBookingService.deleteBookingbyId(bookingId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{bookingId}/Bookings")
	public void findBookingsbyId( @PathVariable int bookingId){
		studioBookingService.findBookingbyId(bookingId);
	}

}
