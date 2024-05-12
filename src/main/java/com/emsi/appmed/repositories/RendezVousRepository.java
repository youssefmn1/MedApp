package com.emsi.appmed.repositories;


import com.emsi.appmed.entities.Consultation;
import com.emsi.appmed.entities.RendezVous;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Transactional
public interface RendezVousRepository extends JpaRepository<RendezVous, Integer> {
    Page<RendezVous> findByPatientsIdIn(List<Integer> patientIds, Pageable pageable);
    @Transactional
    void deleteByPatientsId(int patientId);


    Page<RendezVous> findByDate(LocalDate date, PageRequest of);
}