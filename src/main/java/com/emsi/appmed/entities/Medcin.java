package com.emsi.appmed.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Medcin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private int age;

    @OneToMany(mappedBy = "medcins")
    private Collection<Consultation> consultations;

    @OneToMany(mappedBy = "medcins")
    private Collection<RendezVous> rendezvous;
}
