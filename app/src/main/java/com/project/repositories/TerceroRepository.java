package com.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.models.Tercero;

@Repository
public interface TerceroRepository extends JpaRepository<Tercero, Long> {

	boolean existsByCuitl(String cuitl);

	List<Tercero> findByNombreContainingIgnoreCaseOrderByIdAsc(String nombre);
}
