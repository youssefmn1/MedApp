package com.emsi.appmed.web;

import com.emsi.appmed.entities.Patient;
import com.emsi.appmed.repositories.ConsultationRepository;
import com.emsi.appmed.repositories.OrdonnanceRepository;
import com.emsi.appmed.repositories.PatientRepository;
import com.emsi.appmed.repositories.RendezVousRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/patients")
public class PatientController2 {

    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final RendezVousRepository rendezVousRepository;
    private final OrdonnanceRepository ordonnanceRepository;

    @GetMapping
    public String indexPatient(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "keyword", defaultValue = "") String kw,
            Model model) {
        Page<Patient> patientPage = patientRepository.findByNomContains(kw, PageRequest.of(page, size));
        model.addAttribute("listPatients", patientPage.getContent());
        model.addAttribute("pages", new int[patientPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        return "patients";  // Assuming "patients.html" is the template name
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            model.addAttribute("patient", optionalPatient.get());
            model.addAttribute("singlePatientView", true); // Indicate we are viewing a single patient
        } else {
            model.addAttribute("singlePatientView", false); // Indicate we are not viewing a single patient
        }
        return "patients"; // Return the same patients.html template
    }

    @DeleteMapping("/delete/{id}")
    public String deletePatient(@PathVariable("id") int id) {
        ordonnanceRepository.deleteOrdonnancesByPatientId(id);
        consultationRepository.deleteByPatientsId(id);
        rendezVousRepository.deleteByPatientsId(id);
        patientRepository.deleteById(id);
        return "redirect:/patients";  // Redirecting to the patients list after deletion
    }

    @GetMapping("/edit/{id}")
    public String editPatient(@PathVariable("id") int id, Model model) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "formPatient";  // Assuming "formPatient.html" is the template name
        } else {
            return "redirect:/patients";  // Redirecting to the patients list if not found
        }
    }

    @PostMapping
    public String savePatient(@ModelAttribute Patient patient) {
        patientRepository.save(patient);
        return "redirect:/patients";
    }
}
