package com.emsi.appmed.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Ordonnance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Transient // Indique à JPA de ne pas mapper cette propriété à la base de données
    private MultipartFile pdfFile;

    private String pdfFilePath;

    @OneToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;





}
