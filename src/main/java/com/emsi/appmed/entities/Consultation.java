package com.emsi.appmed.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.DATE)
    private Date date;


    @ManyToOne
    @JoinColumn(name = "medecin_id") // Nom de la colonne dans la table Consultation qui fait référence à l'ID du médecin
    private Medcin medcins;
    @ManyToOne
    private Patient patients;

    @OneToOne(mappedBy = "consultation")
    private Ordonnance ordonnance;

    public void setPatient(Patient patient) {
        this.patients = patient;
    }
}

