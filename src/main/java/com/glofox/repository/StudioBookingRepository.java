package com.glofox.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.glofox.entity.Studio;
import com.glofox.entity.StudioBooking;
import com.glofox.entity.StudioClass;

public interface StudioBookingRepository extends CrudRepository<StudioBooking, Integer> {
	
	public List<StudioBooking> findByStudioClass(StudioClass studioClass);
}
