package com.peace.reg.exception;

import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestValidationHandler {
	
	private MessageSource messageSource;
	
	@Autowired
	public RestValidationHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ResponseEntity<FieldValidationErrorDetails> handelValidationError(
			MethodArgumentNotValidException mNotValidException,HttpServletRequest request){
		
		FieldValidationErrorDetails fErrorsDetails = new
										FieldValidationErrorDetails();
		fErrorsDetails.setError_timeStamp(new Date().getTime());
		fErrorsDetails.setError_status(HttpStatus.BAD_REQUEST.value());
		fErrorsDetails.setError_title("Field Validation Error");
		fErrorsDetails.setError_detail("Input Field Validation Faild");
		fErrorsDetails.setError_developer_message(mNotValidException.getClass().getName());
		fErrorsDetails.setError_path(request.getRequestURI());
		
		BindingResult result = mNotValidException.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		
		for(FieldError error: fieldErrors) {
			FieldValidationError fError = processFieldError(error);
			List<FieldValidationError> fValidationErrorlist = 
					fErrorsDetails.getErrors().get(error.getField());
			if(fValidationErrorlist == null) {
				fValidationErrorlist = new ArrayList<>();
			}
			fValidationErrorlist.add(fError);
			fErrorsDetails.getErrors().put(error.getField(), fValidationErrorlist);
		}
		
		return new ResponseEntity<>(fErrorsDetails,HttpStatus.BAD_REQUEST);
	}
	
	//method to process field error
	private FieldValidationError processFieldError(final FieldError error) {
		FieldValidationError fieldValidationError = new FieldValidationError();
		
		if(error != null) {
			Locale currentLocal = LocaleContextHolder.getLocale();
			String msg = messageSource.getMessage(error.getDefaultMessage(),null, currentLocal);
			
			fieldValidationError.setField(error.getField());
			fieldValidationError.setType(MessageType.ERROR);
			fieldValidationError.setMessage(msg);
		}
		return fieldValidationError;
	}

}
