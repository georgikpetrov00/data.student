package com.grandp.data;

import com.grandp.data.user.User;

public class Test {

	static String name = "Georgi";
	static String fname = "Petrov";
	static String email = "email";
	static String id = "0051";
	static String fac = "1212";
	
	public static void main(String[] args) {
		User s = new User(name, fname, email, id);
		
		System.out.println(s.hasAuthority("ROLE_STUDENT"));
	}
	
}
