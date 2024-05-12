package com.emsi.appmed.web;

import com.emsi.appmed.entities.Account;
import com.emsi.appmed.repositories.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @PostMapping("/register")
    public String registerAccount(@ModelAttribute("account") Account account, Model model) {
        // Vérifier si le compte existe déjà
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount != null) {
            model.addAttribute("error", "Ce nom d'utilisateur est déjà utilisé");
            return "test";
        }

        accountRepository.save(account);
        return "redirect:/test";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est déjà connecté
        if (session.getAttribute("username") != null) {
            return "redirect:/indexPatient"; // Rediriger vers une autre page s'il est déjà connecté
        }

        model.addAttribute("account", new Account());
        return "test";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("account") Account account, Model model, HttpSession session) {
        // Vérifier les identifiants de connexion
        Account existingAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (existingAccount == null) {
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
            return "test";
        }

        // Ajouter le nom d'utilisateur à la session
        session.setAttribute("username", existingAccount.getUsername());

        // Gérer la connexion réussie
        return "redirect:/indexPatient";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Supprimer l'attribut de session "username"
        session.removeAttribute("username");
        return "redirect:/test";
    }

}

