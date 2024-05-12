package com.emsi.appmed.web;


import com.emsi.appmed.entities.Consultation;
import com.emsi.appmed.entities.Ordonnance;
import com.emsi.appmed.entities.Patient;

import com.emsi.appmed.entities.RendezVous;
import com.emsi.appmed.repositories.ConsultationRepository;
import com.emsi.appmed.repositories.OrdonnanceRepository;
import com.emsi.appmed.repositories.PatientRepository;
import com.emsi.appmed.repositories.RendezVousRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class PatientController {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ConsultationRepository consultationRepository;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    OrdonnanceRepository ordonnanceRepository;

    @GetMapping("/indexPatient")
    public String indexPatient(Model model,
                               @RequestParam(name = "page",defaultValue = "0") int page,
                               @RequestParam(name = "size",defaultValue = "5") int size,
                               @RequestParam(name = "keyword",defaultValue = "") String kw){
        Page<Patient> patientPage = patientRepository.findByNomContains(kw, PageRequest.of(page,size));
        model.addAttribute("listPatients",patientPage.getContent());
        model.addAttribute("pages",new int[patientPage.getTotalPages()]);// collecter un tableau avec une size de nombre de page
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",kw);
        return "patients";
    }
    @GetMapping("/infoPatient")
    public String findById(@RequestParam("id") Integer id, Model model) {
        // Récupérer le patient par son ID
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();

            // Créer une liste contenant uniquement le patient trouvé
            List<Patient> patientList = new ArrayList<>();
            patientList.add(patient);

            // Créer une Page à partir de la liste contenant uniquement le patient
            Page<Patient> patientPage = new PageImpl<>(patientList);

            model.addAttribute("listPatients", patientPage.getContent());
            model.addAttribute("pages", new int[patientPage.getTotalPages()]);
            model.addAttribute("currentPage", 0); // Page actuelle (0 car une seule page)
            model.addAttribute("keyword", ""); // Ajustez selon vos besoins

            return "patients"; // ou tout autre nom de vue pour afficher les détails du patient
        } else {
            // Gérer le cas où le patient n'est pas trouvé
            return "redirect:/Patient"; // ou tout autre nom de vue pour afficher un message d'erreur
        }
    }



    @GetMapping("/deletePatient")
    public String deletePatient(@RequestParam(name = "id") int id,
                                @RequestParam(name ="keyword",defaultValue ="") String keyword,
                                @RequestParam(name ="page",defaultValue ="0") int page){
        // Supprimer les ordonnances associées aux consultations du patient
        ordonnanceRepository.deleteOrdonnancesByPatientId(id);
        // Supprimer les consultations associées au patient
        consultationRepository.deleteByPatientsId(id);
        // Supprimer les rendez-vous associés au patient
        rendezVousRepository.deleteByPatientsId(id);

        // Supprimer le patient
        patientRepository.deleteById(id);
        return "redirect:/indexPatient?page="+page+"&keyword="+keyword;
    }



    @GetMapping("/Patient")
    public String index(){
        return "redirect:/indexPatient";
    }
    @GetMapping("/editPatient")
    public String editPatient(@RequestParam("id") int id, Model model) {
        Patient patient = patientRepository.findById(id).orElse(null);
        model.addAttribute("patient", patient);
        return "formPatient";
    }

    @GetMapping("/formPatient")
    public String formPatient(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatient";
    }
    @PostMapping("/savePatient")
    public String saveEtudiant(Patient patient){
        patientRepository.save(patient);
        return "redirect:/Patient";
    }
}
