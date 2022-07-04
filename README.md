# Studio Classes and Bookings Creation

# Project description:

1. The Application provides mainly three functional interfaces to create a studio. 
   A Studio Owner can create a Studio interface for him and add Classes to his Studio with details like 
   Class Name, Start Date and End Dates of the Class, Capacity, Trainer, Desccription of the Class
2. A member of the Studio can book a class. He can use the bookings API where he/she can resister their 
   name,Booking Date


# How to run this project

  ## Pre-requisities

  Java SE Development Kit 11 or newer
  Maven 3.0 or newer

  Check for the versions 

  javac -version
  mvn -version

## Building the Project

  * You can navigate to the root folder of the application and run : 'mvn clean install'

## Running the Application
 
  ** You can navigate to the root folder of the application and run : 'mvn spring-boot:run'


## Access the REST APIs

   ### <u>Studio API:</u>

      POST: localhost:5000/Studio
      Description: Studio Owner can Either Only Add  their Studio or Both their Studio and classes in a single API
	
	* Studio                                                       
	
	 {
      "name": "GlofoxStudio",
      "country": "Ireland",
      "address": "Athlone Deerapark Road",
      "phone": "35389",
      "emailAddress": "asdf@gmail.com"
    }

	* Studio With Classes

     {
      "name": "GlofoxStudio",
      "country": "Ireland",
      "address": "Athlone Deerapark Road",
      "phone": "35389",
      "emailAddress": "asdf@gmail.com",
      "studioclass": [
      {
      		"class_id": "1",
    		"name": "pilates",
    		"capacity": "10",
    		"startDate": "2022-07-03",
    		"endDate": "2022-07-19",
    		"trainer": "Trainer John",
    		"description": "Aerobics is a body fitness activity",
    		"facility": "Sports Center"
      ]
     }


   ### <u>Classes API for a Studio:</u>

      POST: localhost:8080/{studioName}/Classes
      Description: Studio Owner can introduce new Classes to their existing Studio or extend their Classes to their Studio

    {
      "className": "Aerobics",
      "capacity": 10,
      "startDate": "2022-07-03",
      "endDate": "2022-07-19",
      "trainer": "Trainer John",
      "description": "Aerobics is a body fitness activity",
      "facility": "Sports Center"
    }

   ### <u>Bookings API for a Class in a Studio:</u>

      POST: localhost:8080/{studioName}/{className}/Bookings
      Description: Studio Owner can introduce new Classes to their existing Studio or extend their Classes to their Studio

	{
          "date": "05-07-2022",
          "name": "Customer 1"
        }
 
 #Three ways to Access the Application
 
 	1. In the Project Folder 
 	   mvn spring-boot:run
 	   Accesss the Apis on http://localhost:5000/
 	2. Run the Project as Docker Container using
 	   docker-compose up --build  (This will build a impage on Java Alpine and Containerise it)
 	   Accesss the Apis on http://localhost:8887/
 	3. Access the Service from AWS URL
 	   http://studioapplication-env.eba-2qvvr5ad.eu-west-1.elasticbeanstalk.com/swagger-ui/index.html
 	   The Applcation is deployed to AWS on Elastic Bean Stalk.
        
# Key Features:
     * Containarized the Application with Docker. Further improvements and Automation 
       can be done by adding Kubernetes and helm integration
     * Tried to Address the concurrent access for Bookings. 
       A practical business scenario is concurrent usage of the Bookings service specifically
     * A data Model where a class cannot exist without a Studio and a Booking cannot exist to its class and a studio can exist independently.
     * Test Coverage with Jacaco. 
     * Swagger UI for showing the APIs provided
     * A runtime in memory H2 Database
     * Centralised and Unifrom Error Response Mechanism
    

# Scope for Improvements:

    * Front end to book classes and its bookings.
    * Seperate Objects for Model and Entity.  Use DTO pattern to create seperate Model Objects. SO that Entity will not be 
      shown to external users.
    * Integrate the Application with Front End UI to make the app interactive
    * Test Code Coverage percentage for the Application is 82% . 
      Further tests can be included 
    * Add Authentication for restapis. Add seperate authorization for Studio Owner API's (Studio and Class)
      and seperate for Bookings API (Typically APi used by the customer)
    

