package com.emsi.appmed.web;

import com.emsi.appmed.entities.Consultation;
import com.emsi.appmed.entities.Ordonnance;
import com.emsi.appmed.repositories.ConsultationRepository;
import com.emsi.appmed.repositories.OrdonnanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@AllArgsConstructor
@Controller
public class OrdonnanceController {

    @Autowired
    private OrdonnanceRepository ordonnanceRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @GetMapping("/indexOrdonnance")
    public String indexOrdonnance(Model model,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "5") int size,
                                  @RequestParam(name = "consultationId", required = false) Integer consultationId) {
        Page<Ordonnance> ordonnancePage;
        if (consultationId != null) {
            ordonnancePage = ordonnanceRepository.findAllByConsultationId(consultationId, PageRequest.of(page, size));
        } else {
            ordonnancePage = ordonnanceRepository.findAll(PageRequest.of(page, size));
        }
        model.addAttribute("ordonnanceList", ordonnancePage.getContent());
        model.addAttribute("pages", new int[ordonnancePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("consultationId",consultationId);
        return "ordonnances";
    }


    @GetMapping("/deleteOrdonnance")
    public String deleteOrdonnance(@RequestParam(name = "id") int id,
                                   @RequestParam(name = "page", defaultValue = "0") int page) {
        ordonnanceRepository.deleteById(id);
        return "redirect:/indexOrdonnance?page=" + page;
    }

    @GetMapping("/formOrdonnance")
    public String formOrdonnance(Model model, @RequestParam(name = "consultationId", required = false) Integer consultationId) {
        List<Consultation> consultationList = consultationRepository.findAll();
        Ordonnance ordonnance = new Ordonnance();
        if (consultationId != null) {
            ordonnance.setConsultation(consultationRepository.findById(consultationId).orElse(null));
        }
        model.addAttribute("ordonnance", ordonnance);
        model.addAttribute("consultationList", consultationList);
        return "formOrdonnance";
    }



    @PostMapping("/saveOrdonnance")
    public String saveOrdonnance(@ModelAttribute Ordonnance ordonnance, @RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam("consultationId") int consultationId) {
        try {
            // Vérifier si un fichier a été fourni
            if (!pdfFile.isEmpty()) {
                // Obtenir le nom du fichier original
                String fileName = StringUtils.cleanPath(pdfFile.getOriginalFilename());
                // Définir le chemin complet du fichier dans le dossier de destination
                String uploadDirPath = "C:\\Users\\Administrator\\IdeaProjects\\AppMed\\src\\ordonnances\\";
                Path uploadDir = Paths.get(uploadDirPath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                // Créer le chemin complet du fichier dans le dossier de destination
                Path filePath = uploadDir.resolve(fileName);
                // Copier le fichier vers le dossier de destination
                Files.copy(pdfFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                // Mettre à jour l'entité Ordonnance avec le chemin du fichier
                ordonnance.setPdfFilePath(filePath.toString());
                // Afficher le chemin du fichier copié
                System.out.println("Fichier copié dans : " + filePath);
            }
            // Vérifier si une ordonnance avec le même ID de consultation existe déjà
            if (ordonnanceRepository.existsByConsultationId(consultationId)) {
                // Gérer le cas où une ordonnance avec le même ID de consultation existe déjà
                // Vous pouvez afficher un message d'erreur ou rediriger l'utilisateur vers une autre page
            } else {
                // Récupérer la consultation correspondante à partir de consultationId
                Consultation consultation = consultationRepository.findById(consultationId).orElseThrow(() -> new IllegalArgumentException("Invalid consultation ID: " + consultationId));
                // Définir la consultation pour l'ordonnance
                ordonnance.setConsultation(consultation);
                // Enregistrer l'entité Ordonnance
                ordonnanceRepository.save(ordonnance);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception
        }

        return "redirect:/indexOrdonnance";
    }

    @GetMapping("/editOrdonnance")
    public String editOrdonnance(@RequestParam("id") int id, Model model) {
        Ordonnance ordonnance = ordonnanceRepository.findById(id).orElse(null);
        if (ordonnance == null) {
            // Gérer le cas où l'ordonnance n'est pas trouvée
            return "redirect:/indexOrdonnance";
        }

        // Récupérer l'ID de la consultation
        int consultationId = ordonnance.getConsultation() != null ? ordonnance.getConsultation().getId() : null;

        model.addAttribute("ordonnance", ordonnance);
        model.addAttribute("consultationId", consultationId);
        List<Consultation> consultationList = consultationRepository.findAll();
        model.addAttribute("consultationList", consultationList);
        return "formOrdonnance";
    }

    @GetMapping("/viewPdf")
    public ResponseEntity<byte[]> viewPdf(@RequestParam("ordonnanceId") int ordonnanceId) {
        Ordonnance ordonnance = ordonnanceRepository.findById(ordonnanceId).orElseThrow(() -> new RuntimeException("Ordonnance not found"));
        String filePath = ordonnance.getPdfFilePath();

        try {
            // Vérifier si le fichier existe
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("Le fichier PDF n'existe pas : " + filePath);
            }

            // Lire le contenu du fichier PDF en tant qu'octets
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // Renvoyer les octets du fichier PDF dans la réponse
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la lecture du PDF".getBytes());
        }
    }


}
