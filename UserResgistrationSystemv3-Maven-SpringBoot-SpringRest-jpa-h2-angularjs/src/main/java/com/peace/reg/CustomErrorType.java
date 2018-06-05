package com.peace.reg;


import com.peace.reg.dto.UserDTO;

public class CustomErrorType extends UserDTO {
	
	
	public CustomErrorType(final String errorMessage) {
		super.errorMessage = errorMessage;
	}

	

}
