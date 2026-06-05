package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.models.Tercero;
import com.vaadin.flow.data.provider.DataProvider;

@Repository
public interface TerceroRepository extends JpaRepository<Tercero,Long>{

	List<Tercero> findAllByOrderByIdAsc();
	boolean existsByCuitl(String cuitl);
	List<Tercero> findByNombreContainingIgnoreCaseOrderByIdAsc(String nombre);
}
