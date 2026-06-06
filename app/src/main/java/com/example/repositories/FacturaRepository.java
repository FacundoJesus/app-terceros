package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.models.Factura;



@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
	@Query("""
			SELECT DISTINCT f
			FROM Factura f
			LEFT JOIN FETCH f.tercero
			LEFT JOIN FETCH f.items
			""")
			List<Factura> findAllWithDetails();
}
