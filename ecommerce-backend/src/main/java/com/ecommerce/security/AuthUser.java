package com.ecommerce.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.entities.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthUser implements UserDetails {

	private User user;

	/**
	 * Returns the authorities granted to the user for authentication and
	 * authorization purposes.
	 * 
	 * This method is used by Spring Security to retrieve the roles or permissions
	 * assigned to the user. In this implementation, the user's role is mapped to a
	 * SimpleGrantedAuthority, which Spring Security uses to manage access control.
	 * 
	 * @return A collection of GrantedAuthority objects representing the user's
	 *         roles or permissions.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new SimpleGrantedAuthority(user.getRole().name()));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
