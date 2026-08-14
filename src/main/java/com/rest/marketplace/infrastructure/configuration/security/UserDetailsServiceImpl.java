package com.rest.marketplace.infrastructure.configuration.security;

import com.rest.marketplace.domain.ports.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserPort userPort;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userPort.findByEmail(email).map(user -> new User(
				user.getEmail(),
				user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) // El prefijo ROLE_ es requerido por Spring Security
		)).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
	}
}
