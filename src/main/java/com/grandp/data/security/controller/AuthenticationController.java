package com.grandp.data.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@Controller
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login-error")
    public String onFailedLogin(@ModelAttribute("username") String userName,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("username", userName);
        redirectAttributes.addFlashAttribute("badCredentials", true);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        model.addAttribute("activePage", "profile");
        return "profile";
    }

    @GetMapping("/logouted")
    public String showLogoutPage(Model model) {
        return "logouted";
    }

}
