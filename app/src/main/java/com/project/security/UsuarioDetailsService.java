package com.project.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.models.Usuario;
import com.project.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
@Service
public class UsuarioDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
	this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow(() ->
	            												new UsernameNotFoundException("Usuario no encontrado"));
	
	return User.builder()
	        .username(usuario.getNombreUsuario())
	        .password(usuario.getPassword())
	        .roles(usuario.getRolUsuario().name())
	        .build();
	}
	
	
	
	
}
