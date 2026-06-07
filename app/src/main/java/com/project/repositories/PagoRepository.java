package com.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.models.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

}
