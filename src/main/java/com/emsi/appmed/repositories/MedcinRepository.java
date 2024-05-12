package com.emsi.appmed.repositories;

import com.emsi.appmed.entities.Medcin;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface MedcinRepository extends JpaRepository<Medcin,Integer> {
    Page<Medcin> findByNomContains(String keyword, Pageable pageable); // pageable sert pour faire la pagination

}