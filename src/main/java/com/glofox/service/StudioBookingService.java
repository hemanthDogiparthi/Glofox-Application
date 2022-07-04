package com.glofox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glofox.entity.StudioBooking;
import com.glofox.repository.StudioBookingRepository;

@Service
public class StudioBookingService {

	  	@Autowired
	    StudioBookingRepository bookingRepository;

	    public void save(StudioBooking booking) {
	    	bookingRepository.save(booking);
	    }
	    
	    public List<StudioBooking> getAllBookings() {
	        return (List<StudioBooking>) bookingRepository.findAll();
	    }
	    
	    public StudioBooking findBookingbyId(int id) {
	        return  bookingRepository.findById(id).get();
	    }
	    
	    public void deleteBookingbyId(int id) {
	          bookingRepository.deleteById(id);
	    }
}
