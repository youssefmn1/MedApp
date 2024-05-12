package com.emsi.appmed.repositories;

import com.emsi.appmed.entities.Medcin;
import com.emsi.appmed.entities.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Page<Patient> findByNomContains(String keyword, Pageable pageable); // pageable sert pour faire la pagination

    List<Patient> findByNomContaining(String keyword);
}
