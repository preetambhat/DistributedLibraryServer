package com.opteamix.library.service;

import com.opteamix.library.common.AddressVO;
import com.opteamix.library.common.User;
import com.opteamix.library.exception.LibraryException;

public interface ILoginService {

	public User login(User user) throws LibraryException;

	public User signUp(User user) throws LibraryException;

	public User signIn(User user) throws LibraryException;

	public User getUserById(Integer userId) throws LibraryException;

	public AddressVO addAddress(AddressVO address) throws LibraryException;
	
}
