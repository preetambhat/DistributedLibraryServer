package com.opteamix.library.dao;

import com.opteamix.library.common.AddressVO;
import com.opteamix.library.common.User;
import com.opteamix.library.exception.LibraryException;

public interface ILoginDAO {
	
	public boolean isUserExisting(User user) throws LibraryException;
	
	public User createUser(User user);
	
	public void updateUser(User user);


	public User signUp(User user) throws LibraryException;

	public User signIn(User user) throws LibraryException;

	public User getUserById(Integer userId) throws LibraryException;

	public AddressVO addAddress(AddressVO address) throws LibraryException;
}
