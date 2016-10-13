package com.opteamix.library.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.opteamix.library.common.LibraryConstants;
import com.opteamix.library.common.User;
import com.opteamix.library.exception.LibraryException;
import com.opteamix.library.service.ILoginService;

public abstract class AbstractBaseController {

	@Autowired 
	public ILoginService loginService;
	
	public User validateUserAccess(Integer userId,String authToken) throws LibraryException{
		User user = null;
		try{
			if(userId == null || authToken == null){
				throw new LibraryException(LibraryConstants.MANDATORY_SERVICE_VARIABLES_MISSING,LibraryConstants.MANDATORY_SERVICE_VARIABLES_MISSING_CODE);
			}
			user = loginService.getUserById(userId);
			if(user == null){
				throw new LibraryException(LibraryConstants.USER_DOES_NOT_EXIST,LibraryConstants.USER_DOES_NOT_EXIST_CODE);
			}else if(!user.getAuthToken().equals(authToken)){
				throw new LibraryException(LibraryConstants.SESSION_EXPIRED,LibraryConstants.SESSION_EXPIRED_CODE);
			}
			
		}catch(LibraryException e){
			throw e;
		}catch(Exception e){
			throw new LibraryException(LibraryConstants.UNEXPECTED_ERROR_MESSAGE,LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return user;
	}
	
}
