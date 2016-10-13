package com.opteamix.library.util;

public class RandomNumberUtil {
	static final String ALFANUM ="0123456789ABCDEFGHIJKLMNOP";
	/**
	 * This method is used to generate Access token for Application 
	 * @return
	 */
	public static String generateAppAccessToken()
	{
		final Integer size = 248;
		StringBuilder sb = new StringBuilder(size);
		for (int i=0;  i<size;  i++) {
			int ndx = (int)(Math.random()*ALFANUM.length());
			sb.append(ALFANUM.charAt(ndx));
		}
		return sb.toString();
	}

}
