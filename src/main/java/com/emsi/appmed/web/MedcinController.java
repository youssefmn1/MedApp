package com.emsi.appmed.web;

import com.emsi.appmed.entities.Patient;
import lombok.AllArgsConstructor;

import com.emsi.appmed.entities.Medcin;
import com.emsi.appmed.repositories.MedcinRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class MedcinController {

    @Autowired
    MedcinRepository medcinRepository;

    @GetMapping("/indexMedecin")
    public String indexMedecin(Model model,
                               @RequestParam(name = "page",defaultValue = "0") int page,
                               @RequestParam(name = "size",defaultValue = "5") int size,
                               @RequestParam(name = "keyword",defaultValue = "") String kw){
        //Page<Medcin> medcinPage = medcinRepository.findAll(PageRequest.of(page,size));
        Page<Medcin> medcinPage = medcinRepository.findByNomContains(kw,PageRequest.of(page,size));
        model.addAttribute("listMedecins",medcinPage.getContent());
        model.addAttribute("pages",new int[medcinPage.getTotalPages()]);// collecter un tableau avec une size de nombre de page
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",kw);
        return "medcins";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam(name = "id") int id,
                         @RequestParam(name ="keyword",defaultValue ="") String keyword,
                         @RequestParam(name ="page",defaultValue ="0") int page){
        medcinRepository.deleteById(id);
        return "redirect:/indexMedecin?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/editMedcin")
    public String editMedecin(@RequestParam("id") int id, Model model) {
        Medcin medcin = medcinRepository.findById(id).orElse(null);
        model.addAttribute("medcin", medcin);
        return "formMedcin";
    }
    @GetMapping("/home")
    public String home() {
        return "index";
    }
    @GetMapping("/test")
    public String test() {
        return "test";
    }
    @GetMapping("/")
    public String index(){
        return "redirect:/login";
    }

    @GetMapping("/medcins")
    public String medcins(){
        return "redirect:/indexMedecin";
    }

    @GetMapping("/formMedcin")
    public String formEtudiant(Model model){
        model.addAttribute("medcin",new Medcin());
        return "formMedcin";
    }
    @PostMapping("/saveMedcin")
    public String saveEtudiant(@ModelAttribute("medcin") Medcin medcin) {
        medcinRepository.save(medcin);
        return "redirect:/indexMedecin";
    }

}
