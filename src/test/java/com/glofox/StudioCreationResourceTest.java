package com.glofox;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glofox.entity.Studio;
import com.glofox.entity.StudioBooking;
import com.glofox.entity.StudioClass;
import com.glofox.repository.StudioBookingRepository;
import com.glofox.repository.StudioClassRepository;
import com.glofox.repository.StudioRepository;
import com.glofox.service.StudioService;
import com.glofox.validator.InputValidator;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.util.ClassUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		 classes = GlofoxApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application.properties")
public class StudioCreationResourceTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	StudioService studioService;
	
	@Autowired
	InputValidator studioInputValidator;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	StudioBookingRepository studioBookingrepo;
	
	@Autowired
	StudioClassRepository studioClassrepo;
	
	@Autowired
	StudioRepository studioRepo;
	
	@AfterTestMethod
    public void cleanup() {
		studioBookingrepo.deleteAll();
		studioClassrepo.deleteAll();
		studioRepo.deleteAll();
    }
	
	@Test
    public void givenStudioCreated_thenStatusis200() throws Exception {
        //given+
		
		
		Studio studio = createStudio();
		
        this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		
        this.mockMvc.perform(get("/Studio").contentType("application/json")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("asdf@gmail.com")));
        
        cleanup();
	 }
	
	@Test
    public void givenStudioCreatedWithInsufficientParams_thenBadRequest() throws Exception {
        Studio studio = new Studio();
        studio.setStudio_id(1);
        studio.setEmailAddress("asdf@gmail.com");
        this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("missing")));
        
        cleanup();
	 }
	
	
	@Test
    public void givenStudioCreatedWithStudioClasses_thenStatusis200() throws Exception {	
        
		
		Studio studio = createStudiowithClasses();
        
        this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		
        this.mockMvc.perform(get("/Studio").contentType("application/json")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("asdf@gmail.com")));
        
        cleanup();
	 }
	
	@Test
    public void givenClassesCreated_then200Success() throws Exception {
        //given
		Studio studio = createStudio();
		this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		
		List<StudioClass> studioClass = createClasses();
		this.mockMvc.perform(post("/GlofoxStudio/Classes").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioClass.get(0))));
		
	
        this.mockMvc.perform(get("/GlofoxStudio/Classes")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Aerobics")));
        
        cleanup();
    }
	
	
	@Test
    public void givenClasseCreatedWithInsufficientParams_thenBadRequest() throws Exception {
        //given
        StudioClass studioClass = new StudioClass();
        studioClass.setStartDate(LocalDate.now());
        studioClass.setEndDate(LocalDate.ofYearDay(2022, 200));
        
       
        this.mockMvc.perform(post("/GlofoxStudio/Classes").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioClass)))
        		.andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("missing")));
        
        cleanup();
    }
	
	
	@Test
    public void givenClassCreatedWithNoParentStudio_thenResourceNotFound() throws Exception {
        //given
		List<StudioClass> studioClass = createClasses();
        
        this.mockMvc.perform(post("/GlofoxStudio2/Classes").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioClass.get(0))))
        .andExpect(status().isNotFound())	
        .andExpect(content().string(containsString("No")));
        
        cleanup();
    }
	
	
	@Test
    public void givenBookingsCreatedWithNoParentClass_thenResourceNotFound() throws Exception {
        //given
		Set<StudioBooking> studioBookings = createBookings();
        
        this.mockMvc.perform(post("/GlofoxStudio2/Aerobics/Bookings").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioBookings.stream().findFirst().get())))
        .andExpect(status().isNotFound())	
        .andExpect(content().string(containsString("No")));
        
        cleanup();
    }
	
	@Test
    public void givenBookingsCreatedWithWithInsufficientParams_thenBadRequest() throws Exception {
        //given
		StudioBooking studioBooking = new StudioBooking();
		studioBooking.setName("Customer 1");
		
        this.mockMvc.perform(post("/GlofoxStudio/Aerobics/Bookings").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioBooking)))
        .andExpect(status().isBadRequest())	
        .andExpect(content().string(containsString("missing")));
        
        cleanup();
    }
	
	
	@Test
    public void givenBookingsCreatedWithWithNoParentClass_thenResourceNotFound() throws Exception {
        //given
		StudioBooking studioBooking = new StudioBooking();
		studioBooking.setName("Customer 1");
		studioBooking.setDate(LocalDate.now());
		
        this.mockMvc.perform(post("/GlofoxStudio2/Aerobics/Bookings").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioBooking)))
        .andExpect(status().isNotFound())	
        .andExpect(content().string(containsString("No")));
        
        cleanup();
    }
	
	@Test
    public void givenBookingsCreatedCallWithwrongClass__whenpostBooking_thenResourceNotFound() throws Exception {
        //given
		Studio studio = createStudiowithClassesAndBookings();
		this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		Set<StudioBooking> studioBookings = createBookings();
        this.mockMvc.perform(post("/GlofoxStudio/WrongClass/Bookings").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioBookings.stream().findFirst().get())))
        .andExpect(status().isNotFound())	
        .andExpect(content().string(containsString("No Classes")));
        
        cleanup();
    }
	
	@Test
    public void givenBookingsWithInavlidDate__whenpostBooking_thenBadRequest() throws Exception {
        //given
		Studio studio = createStudiowithClasses();
		this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		
		StudioBooking studioBooking = new StudioBooking();
		studioBooking.setName("Customer 1");
		studioBooking.setDate(LocalDate.now().minusDays(2));
		
		this.mockMvc.perform(post("/GlofoxStudio/Aerobics/Bookings").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioBooking)))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("StudioBooking Date is not within the Scheduled Classes")));
		
		cleanup();
    }
	
	@Test
    public void givenBookingsCreatedWithStudiAndClasses_whenSearchforBookings_then200Success() throws Exception {
        //given
		Studio studio = createStudiowithClasses();
		this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		
		Set<StudioBooking> studioBookings = createBookings();
		
		String ifMatch = "\"" + 0 + "\"";
		this.mockMvc.perform(post("/GlofoxStudio/Aerobics/Bookings")
				.header("If-Match",ifMatch )
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioBookings.stream().findFirst().get())));

		
	
        this.mockMvc.perform(get("/GlofoxStudio/Aerobics/Bookings")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Customer")));
        
        cleanup();
    }
	
	
	@Test
    public void givenConcurrentlyModifyingStudioBookings_thenPreConditionFailed() throws Exception {
        //given
		Studio studio = createStudiowithClasses();
		this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		
		Set<StudioBooking> studioBookings = createBookings();
		
		String ifMatch = "\"" + 1 + "\"";
		this.mockMvc.perform(post("/GlofoxStudio/Aerobics/Bookings")
				.header("If-Match",ifMatch )
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtils.toJson(studioBookings.stream().findFirst().get())))
				.andDo(print())
				.andExpect(status().isPreconditionFailed())
				.andExpect(content().string(containsString("The Bookings version changed Use this Updated parameter")));

		
	
        
        
        cleanup();
    }
	
	@Test
    public void givenBookingsCreated_whenWithoutRequestHeaderETag_thenInvalidRequest() throws Exception {
        //given
		Studio studio = createStudiowithClasses();
		this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		
		Set<StudioBooking> studioBookings = createBookings();
		
		
		this.mockMvc.perform(post("/GlofoxStudio/Aerobics/Bookings")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioBookings.stream().findFirst().get())))
		.andExpect(status().isBadRequest())
		.andExpect(content().string(containsString("Etag")));
        
        cleanup();
    }
	
	@Test
    public void givenClassWithBookings_whenSearchClasses_then200Success() throws Exception {
        //given
		Studio studio = createStudio();
		this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		List<StudioClass> studioclasses = createClassesWithBookings();
		this.mockMvc.perform(post("/GlofoxStudio/Classes").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioclasses.get(0))));
		
		this.mockMvc.perform(get("/Studio")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("GlofoxStudio")));
		
        this.mockMvc.perform(get("/GlofoxStudio/Classes")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Aerobics")));
        
        cleanup();
    }
	
	
	@Test
    public void givenClassWithBookings_WhenFindClassByName_then200Success() throws Exception {
        //given
		Studio studio = createStudio();
		this.mockMvc.perform(post("/Studio").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studio)));
		
		List<StudioClass> studioclasses = createClassesWithBookings();
		this.mockMvc.perform(post("/GlofoxStudio/Classes").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(studioclasses.get(0))));
		
		this.mockMvc.perform(get("/ClassName/Aerobics/Classes")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("Aerobics")));
		
        
        cleanup();
    }
	
	private Studio createStudiowithClassesAndBookings() {
		 Studio studio = createStudio();
		 studio.getStudioclass().addAll(createClasses());
		 studio.getStudioclass().forEach(c -> c.setStudioBooking(createBookings()));
		 return studio;
	}
	
	
	
	private Studio createStudiowithClasses() {
		 Studio studio = createStudio();
		 studio.getStudioclass().addAll(createClasses());
		 return studio;
	}
	
	private List<StudioClass> createClassesWithBookings() {
		List<StudioClass> classes = createClasses();
		classes.forEach(c -> c.setStudioBooking(createBookings()));
		return classes;
	}
	
	
	private Studio createStudio() {
		Studio studio = new Studio();
        studio.setStudio_id(1);
        studio.setName("GlofoxStudio");
        studio.setCountry("Ireland");
        studio.setEmailAddress("asdf@gmail.com");
        return studio;
	}
	
	private List<StudioClass> createClasses() {
		StudioClass studioClass1 = new StudioClass();
        studioClass1.setClassName("Aerobics");
        studioClass1.setCapacity(10);
        studioClass1.setStartDate(LocalDate.now());
        studioClass1.setEndDate(LocalDate.ofYearDay(2022, 200));
       
        List<StudioClass> list= new ArrayList<>();
        list.add(studioClass1);
        return list;
	}
	
	private Set<StudioBooking> createBookings() {
		StudioBooking studioBooking1 = new StudioBooking();
		studioBooking1.setName("Customer 1");
		studioBooking1.setDate(LocalDate.now().plusDays(2));
		studioBooking1.setBooking_id(0);
		
		StudioBooking studioBooking2 = new StudioBooking();
		studioBooking2.setName("Customer 2");
		studioBooking2.setDate(LocalDate.now().plusDays(3));
		studioBooking2.setBooking_id(1);
		
		Set<StudioBooking> list= new HashSet<>();
        list.add(studioBooking1);
        list.add(studioBooking2);
        return list;	
	}
}


