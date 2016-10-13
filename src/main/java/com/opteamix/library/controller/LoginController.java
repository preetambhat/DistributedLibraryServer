package com.opteamix.library.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.opteamix.library.common.AddressVO;
import com.opteamix.library.common.LibraryConstants;
import com.opteamix.library.common.User;
import com.opteamix.library.exception.LibraryException;
import com.opteamix.library.service.ILoginService;

@Controller
@RequestMapping("/login")
public class LoginController extends AbstractBaseController {
	
	private static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	private ILoginService loginService;
	
	@RequestMapping(value="/signup",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> signUp(@RequestBody User user){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			User responseUser = loginService.signUp(user);
			responseMap.put(LibraryConstants.OBJECT,responseUser);
			responseMap.put(LibraryConstants.STATUS,LibraryConstants.SUCCESS);
			responseMap.put(LibraryConstants.STATUS_CODE,LibraryConstants.SUCCESS_CODE);
		}catch(LibraryException e){
			logger.error("Library Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, e.getMessage());
			responseMap.put(LibraryConstants.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			logger.error("Generic Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, LibraryConstants.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(LibraryConstants.ERROR_CODE, LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	
	@RequestMapping(value="/signin",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> signIn(@RequestBody User user){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			User responseUser = loginService.signIn(user);
			responseMap.put(LibraryConstants.OBJECT,responseUser);
			responseMap.put(LibraryConstants.STATUS,LibraryConstants.SUCCESS);
			responseMap.put(LibraryConstants.STATUS_CODE,LibraryConstants.SUCCESS_CODE);
		}catch(LibraryException e){
			logger.error("Library Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, e.getMessage());
			responseMap.put(LibraryConstants.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			logger.error("Generic Exeption : ",e);
			responseMap.put(LibraryConstants.MESSAGE, LibraryConstants.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(LibraryConstants.ERROR_CODE, LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	
	
	@RequestMapping(value="/getUserById",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> getUserById(@RequestBody User user){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			validateUserAccess(user.getUserId(),user.getAuthToken());
			User responseUser = loginService.getUserById(user.getUserId());
			responseMap.put(LibraryConstants.OBJECT,responseUser);
			responseMap.put(LibraryConstants.STATUS,LibraryConstants.SUCCESS);
			responseMap.put(LibraryConstants.STATUS_CODE,LibraryConstants.SUCCESS_CODE);
		}catch(LibraryException e){
			responseMap.put(LibraryConstants.MESSAGE, e.getMessage());
			responseMap.put(LibraryConstants.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			responseMap.put(LibraryConstants.MESSAGE, LibraryConstants.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(LibraryConstants.ERROR_CODE, LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	
	
	@RequestMapping(value="/addAddress",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> addAddress(@RequestBody AddressVO address){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			validateUserAccess(address.getUserId(),address.getAuthToken());
			AddressVO responseAddress = loginService.addAddress(address);
			responseMap.put(LibraryConstants.STATUS,LibraryConstants.SUCCESS);
			responseMap.put(LibraryConstants.STATUS_CODE,LibraryConstants.SUCCESS_CODE);
		}catch(LibraryException e){
			responseMap.put(LibraryConstants.MESSAGE, e.getMessage());
			responseMap.put(LibraryConstants.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			responseMap.put(LibraryConstants.MESSAGE, LibraryConstants.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(LibraryConstants.ERROR_CODE, LibraryConstants.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	
	
}
