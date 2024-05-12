package com.emsi.appmed.repositories;

import com.emsi.appmed.entities.Consultation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

@Transactional
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    Page<Consultation> findByPatientsId(Integer patientId, Pageable pageable);

    Page<Consultation> findByPatientsIdIn(List<Integer> patientIds, Pageable pageable);

    @Transactional
    void deleteByPatientsId(int patientId);


}


