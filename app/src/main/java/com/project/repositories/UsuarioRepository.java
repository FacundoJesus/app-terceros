package com.project.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
	

    boolean existsByNombreUsuario(String nombreUsuario);
    
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    
    List<Usuario> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);

	
}
