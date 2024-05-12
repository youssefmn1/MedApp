package com.emsi.appmed.repositories;

import com.emsi.appmed.entities.Consultation;
import com.emsi.appmed.entities.Ordonnance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrdonnanceRepository extends JpaRepository<Ordonnance,Integer> {

    Page<Ordonnance> findAllByConsultationId(int consultationId, Pageable pageable);

    boolean existsByConsultationId(int consultationId);

    @Modifying
    @Query("DELETE FROM Ordonnance o WHERE o.consultation.patients.id = :patientId")
    void deleteOrdonnancesByPatientId(int patientId);


    @Transactional
    void deleteByConsultationId(int id);
}
