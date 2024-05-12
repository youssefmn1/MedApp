package com.emsi.appmed.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private int age;

    @OneToMany(mappedBy = "patients")
    private Collection<Consultation> consultations;

    @OneToMany(mappedBy = "patients")
    private Collection<RendezVous> rendezvous;
}
