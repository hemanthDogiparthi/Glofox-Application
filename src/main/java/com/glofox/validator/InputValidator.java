package com.glofox.validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.glofox.entity.Studio;
import com.glofox.entity.StudioBooking;
import com.glofox.entity.StudioClass;
import com.glofox.exceptions.ConcurrencyException;
import com.glofox.exceptions.InvalidRequestException;
import com.glofox.exceptions.ServiceException;

@Component
public class InputValidator {

	public void validateInput(Studio studio) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(studio.getName()) ||
				StringUtils.isEmpty(studio.getEmailAddress()) ||
						StringUtils.isEmpty(studio.getCountry())   ) {
			throw new InvalidRequestException("Studio Request missing mandatory fields");
		}
	}
	
	
	public void validateInput(StudioClass studioClass) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(studioClass.getClassName()) ||
				studioClass.getEndDate() == null  ||
				studioClass.getStartDate() == null ||
				studioClass.getCapacity() == 0)  {
			throw new InvalidRequestException("StudioClass Request missing mandatory fields");
		}
	}
	
	public void validateConcurrentRequests(WebRequest webRequest , long eTagVersion, StudioBooking studioBooking) {
		// TODO Auto-generated method stub
		String ifMatchValue = webRequest.getHeader("If-Match");
		
		if (StringUtils.isEmpty(ifMatchValue)) {
			throw new InvalidRequestException("Missing Etag. Fetch it from RequestHeader from call allBookings REST call");
		}
		if (!ifMatchValue.equals("\"" + eTagVersion + "\"")) {
			throw new ConcurrencyException("The Bookings version changed Use this Updated parameter  in the resource Header  " + eTagVersion);
		}
	}

	public void validateInput(StudioBooking studioBooking) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(studioBooking.getName()) ||
				studioBooking.getDate() ==null  )  {
			throw new InvalidRequestException("StudioBooking Request missing mandatory fields");
		}
	}

	public void validateBookingDate(StudioClass studioClass, StudioBooking studioBooking) {
		LocalDate bookingDate = studioBooking.getDate();
		LocalDate classStartDate = studioClass.getStartDate();
		LocalDate classEndDate = studioClass.getEndDate();
				
		 if (bookingDate != null && classStartDate != null && classEndDate != null) {
			 if(bookingDate.isBefore(classEndDate) && bookingDate.isAfter(classStartDate)) {
				 return;
			 }
			 if(bookingDate.equals(classStartDate)  && bookingDate.equals(classEndDate)) {
				 return;
			 }
		 }
		 throw new InvalidRequestException("StudioBooking Date is not within the Scheduled Classes");
	}


	public void validateCapacity(StudioClass studioClass, StudioBooking studioBooking, String studioName) {
		boolean capacityPresent = studioClass.getStudioBooking().stream()
				.collect(Collectors.groupingBy(b -> b.getDate()))
				.getOrDefault(studioBooking.getDate(), new ArrayList<StudioBooking>())
				.size() < studioClass.getCapacity();
		if(!capacityPresent) {
			throw new ServiceException("Capacity Exceeded".concat(studioClass.getClassName()).concat(" for studio ").concat(studioName));
		}
	}
}
