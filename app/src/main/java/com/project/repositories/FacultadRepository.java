package com.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.models.Facultad;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {
	
    List<Facultad> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByCuit(String cuit);
}
