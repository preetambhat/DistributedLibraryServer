package com.opteamix.library.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.opteamix.library.common.AddressVO;
import com.opteamix.library.common.OptiUser;
import com.opteamix.library.common.User;
import com.opteamix.library.dao.ILoginDAO;
import com.opteamix.library.exception.LibraryException;
import com.opteamix.library.service.ILoginService;
import com.opteamix.library.util.RandomNumberUtil;

@Service
public class LoginService implements ILoginService{

	@Autowired
	private Environment envi;
	
	@Autowired
	public ILoginDAO loginDAO;
	
	
	
	@Override
	public User login(User user) throws LibraryException {
		Boolean flag = ldapService(user.getUserEmail(), user.getPassword());

		if (flag == true) {			
			String url = envi.getProperty("opteamizer.supervisorapilocal.url");
			url += user.getUserEmail();
//			JsonUser jUser = getJsonResult(url);
			RestTemplate restTemplate = new RestTemplate();
			 List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		    converters.add(new MappingJackson2HttpMessageConverter());
		    converters.add(new ByteArrayHttpMessageConverter());
		    converters.add(new FormHttpMessageConverter());
		    restTemplate.setMessageConverters(converters);
			String responseString = restTemplate.getForObject(url,String.class);
			Gson gson = new Gson();
			OptiUser optiUser = gson.fromJson(responseString, OptiUser.class);
		
			user.setFirstName(optiUser.getEmployeeFirstName());
			user.setLastName(optiUser.getEmployeeLastName());
			user.setAuthToken(UUID.randomUUID().toString());
			if (loginDAO.isUserExisting(user)) {
//				loginDAO.updateUser(user);				
//			} else {
				loginDAO.createUser(user);
			}
		}
		return user;
	}

	public Boolean ldapService(String userName, String password) {
	      Boolean authenticationFlag;
	      if (!StringUtils.isEmpty(userName)) {
	         try {
	            if ( bind( userName, password ) ) {
	               System.out.println( "user '" + userName + "' authentication succeeded" );
	               authenticationFlag = Boolean.TRUE;
	            }else {
	               System.out.println( "user '" + userName + "' authentication failed" );
	               authenticationFlag = Boolean.FALSE;
	            }
	         } catch (Exception e) {
	            e.printStackTrace();
	            authenticationFlag = Boolean.FALSE;
	         }
	      }else {
	         System.out.println( "user '" + userName + "' not found" );
	         authenticationFlag = Boolean.FALSE;
	      }
	      return authenticationFlag;
	   }

	   private boolean bind (String username, String password) throws Exception {
		      Hashtable<String,String> env = new Hashtable <String,String>();
		      env.put(Context.SECURITY_AUTHENTICATION, "simple");
		      env.put(Context.SECURITY_PRINCIPAL, username);
		      env.put(Context.SECURITY_CREDENTIALS, password);

		      try {
		         ldapContext(env);
		      }
		      catch (javax.naming.AuthenticationException e) {
		         e.printStackTrace();
		         return false;
		      }
		      return true;
		   }
	   

	   private DirContext ldapContext (Hashtable <String,String>env) throws Exception {
	      env.put(Context.INITIAL_CONTEXT_FACTORY, envi.getProperty("context.factory"));
	      env.put(Context.PROVIDER_URL, envi.getProperty("ldap.url"));
	      DirContext ctx = new InitialDirContext(env);
	      System.out.println("ctx:" + ctx);
	      return ctx;
	   }


	@Override
	public User signUp(User user) throws LibraryException {
		String accessToken = RandomNumberUtil.generateAppAccessToken();
		user.setAuthToken(accessToken);
		User responseUser = loginDAO.signUp(user);
		return responseUser;
	}

	@Override
	public User signIn(User user) throws LibraryException {
		User loggedInUser = loginDAO.signIn(user);
		return loggedInUser;
	}

	@Override
	public User getUserById(Integer userId) throws LibraryException {
		return loginDAO.getUserById(userId);
	}

	@Override
	public AddressVO addAddress(AddressVO address) throws LibraryException {
		return loginDAO.addAddress(address);
	}
	
	
	
}
