package com.glofox.exceptions;

import java.util.ArrayList;
import java.util.List;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
 
@ControllerAdvice
public class CentralExceptionHandler extends ResponseEntityExceptionHandler 
{
   
  @ExceptionHandler(InvalidRequestException.class)
  public final ResponseEntity<EntityErrorResponse> handleInvalidRequestException
            (InvalidRequestException ex, WebRequest request) 
  {
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    EntityErrorResponse errorResp = new EntityErrorResponse("INVALID REQUEST", details);
    return new ResponseEntity<>(errorResp, HttpStatus.BAD_REQUEST);
  }
   
  @ExceptionHandler(MissingDataException.class)
  public final ResponseEntity<EntityErrorResponse> handleMissingDataException
            (MissingDataException ex, WebRequest request) 
  {
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    EntityErrorResponse errorResp = new EntityErrorResponse("MISSING DATA", details);
    return new ResponseEntity<>(errorResp, HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<EntityErrorResponse> handleServiceException
            (Exception ex, WebRequest request) 
  {
    List<String> details = new ArrayList<>();
    
    details.add(ex.getLocalizedMessage());
    details.add(ex.toString());
    EntityErrorResponse errorResp = new EntityErrorResponse("Errors", details);
    return new ResponseEntity<>(errorResp, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(ConcurrencyException.class)
  public final ResponseEntity<EntityErrorResponse> handleConcurrentModificationsException
            (Exception ex, WebRequest request) 
  {
    List<String> details = new ArrayList<>();
    
    details.add(ex.getLocalizedMessage());
    details.add(ex.toString());
    EntityErrorResponse errorResp = new EntityErrorResponse("Errors", details);
    
    return new ResponseEntity<>(errorResp, HttpStatus.PRECONDITION_FAILED);
  }
}