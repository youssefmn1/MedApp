package com.emsi.appmed;

import com.emsi.appmed.entities.Consultation;
import com.emsi.appmed.entities.Medcin;
import com.emsi.appmed.entities.Ordonnance;
import com.emsi.appmed.entities.Patient;
import com.emsi.appmed.repositories.ConsultationRepository;
import com.emsi.appmed.repositories.MedcinRepository;
import com.emsi.appmed.repositories.OrdonnanceRepository;
import com.emsi.appmed.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class AppMedApplication implements CommandLineRunner {

    @Autowired
    OrdonnanceRepository ordonnanceRepository;

    MedcinRepository medcinRepository;
    @Autowired

    PatientRepository patientRepository;
    @Autowired

    ConsultationRepository consultationRepository;

    public static void main(String[] args) {
        SpringApplication.run(AppMedApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


    }

}
