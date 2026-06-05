package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.models.Facultad;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {

}
