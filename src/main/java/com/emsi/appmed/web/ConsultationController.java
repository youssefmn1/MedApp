package com.emsi.appmed.web;

import com.emsi.appmed.entities.Consultation;
import com.emsi.appmed.entities.Medcin;
import com.emsi.appmed.entities.Ordonnance;
import com.emsi.appmed.entities.Patient;
import com.emsi.appmed.repositories.ConsultationRepository;
import com.emsi.appmed.repositories.MedcinRepository;
import com.emsi.appmed.repositories.OrdonnanceRepository;
import com.emsi.appmed.repositories.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Controller
public class ConsultationController {
    @Autowired
    private ConsultationRepository consultationRepository;
    @Autowired
    private MedcinRepository medcinRepository;
    @Autowired
    private OrdonnanceRepository ordonnanceRepository;
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/indexConsultation")
    public String indexConsultation(Model model,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "5") int size,
                                    @RequestParam(name = "keyword", required = false) String keyword,
                                    @RequestParam(name = "patientId", required = false) Integer patientId) {
        Page<Consultation> consultationPage;
        if (keyword != null && !keyword.isEmpty()) {
            List<Patient> patients = patientRepository.findByNomContaining(keyword);
            List<Integer> patientIds = patients.stream().map(Patient::getId).collect(Collectors.toList());
            consultationPage = consultationRepository.findByPatientsIdIn(patientIds, PageRequest.of(page, size));
        } else if (patientId != null) {
            consultationPage = consultationRepository.findByPatientsId(patientId, PageRequest.of(page, size));
        } else {
            consultationPage = consultationRepository.findAll(PageRequest.of(page, size));
        }
        model.addAttribute("listConsultations", consultationPage.getContent());
        model.addAttribute("pages", new int[consultationPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("patientList", patientRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("patientId" , patientId);
        return "consultations";
    }

    @GetMapping("/deleteConsultation")
    public String deleteConsultation(@RequestParam(name = "id") int id,
                                     @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                     @RequestParam(name = "page", defaultValue = "0") int page) {
        // Supprimer les ordonnances associées aux consultations du patient
        ordonnanceRepository.deleteByConsultationId(id);

        consultationRepository.deleteById(id);
        return "redirect:/indexConsultation?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/Consultation")
    public String index() {
        return "redirect:/indexConsultation";
    }

    @GetMapping("/editConsultation")
    public String editConsultation(@RequestParam("id") int id, @RequestParam(name = "patientId", defaultValue = "-1") Integer patientId, Model model) {
        Consultation consultation = consultationRepository.findById(id).orElse(null);
        model.addAttribute("consultation", consultation);
        List<Ordonnance> ordonnanceList = ordonnanceRepository.findAll();
        List<Patient> patientList = patientRepository.findAll();
        List<Medcin> medcinList = medcinRepository.findAll();
        assert consultation != null;
        model.addAttribute("medicamentList", ordonnanceList);
        model.addAttribute("patientList", patientList);
        model.addAttribute("medcinList", medcinList);
        model.addAttribute("patientId", patientId); // Ajout de patientId comme paramètre
        return "formConsultation";
    }


    @GetMapping("/formConsultation")
    public String formConsultation(Model model, @RequestParam(name = "patientId", defaultValue = "-1") Integer patientId) {
        Consultation consultation = new Consultation();

        if (patientId != -1) {
            consultation.setPatient(patientRepository.findById(patientId).orElse(null));
        }

        List<Medcin> medcinList = medcinRepository.findAll();
        List<Ordonnance> medicamentList = ordonnanceRepository.findAll();
        List<Patient> patientList = patientRepository.findAll();

        model.addAttribute("consultation", consultation);
        model.addAttribute("medicamentList", medicamentList);
        model.addAttribute("patientList", patientList);
        model.addAttribute("patientId", patientId);
        model.addAttribute("medcinList", medcinList);

        return "formConsultation";
    }






    @PostMapping("/saveConsultation")
    public String saveConsultation(@ModelAttribute @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Consultation consultation) {
        consultationRepository.save(consultation);
        return "redirect:/Consultation";
    }


}
