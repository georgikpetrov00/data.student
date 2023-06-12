package com.grandp.data.entity.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface SimpleUser extends UserDetails {

	public Collection<? extends GrantedAuthority> getAuthorities();
	
	public boolean hasAuthority(String authority);

	public String getPassword();

	public String getUsername();

	public boolean isAccountNonExpired();

	public boolean isAccountNonLocked();

	public boolean isCredentialsNonExpired();

	public boolean isEnabled();

}
