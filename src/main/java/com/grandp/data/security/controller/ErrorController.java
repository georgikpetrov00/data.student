package com.grandp.data.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {

        if (request.getAttribute("errorMessage") != null) {
            String errorMessage = request.getAttribute("errorMessage").toString();
            model.addAttribute("errorMessage", errorMessage);
        }

        return "error";
    }

}