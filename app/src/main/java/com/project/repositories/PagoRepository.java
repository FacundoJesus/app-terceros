package com.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.models.Pago;


@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
	
	
    @Query("""
            SELECT DISTINCT p
            FROM Pago p
            LEFT JOIN FETCH p.tercero
            LEFT JOIN FETCH p.pagosDetalles
            ORDER BY p.id
            """)
    List<Pago> buscarTodosConDetalles();

    @Query("""
            SELECT DISTINCT p
            FROM Pago p
            JOIN FETCH p.tercero t
            LEFT JOIN FETCH p.pagosDetalles
            WHERE LOWER(t.nombre)
            LIKE LOWER(CONCAT('%', :nombre, '%'))
            """)
    List<Pago> buscarPorNombreTercero(@Param("nombre") String nombre);
	
}
