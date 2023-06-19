package com.grandp.data.security.controller;

import com.grandp.data.constants.ErrorPageConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerImpl implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request, HttpServletResponse response) {

        int requestCode = response.getStatus();

        switch (requestCode) {
            case 403:
                model.addAttribute("errorMessage", ErrorPageConstants.STATUS_403);
                break;
            case 404:
                model.addAttribute("errorMessage", ErrorPageConstants.STATUS_404);
                break;
            case  406:
                model.addAttribute("errorMessage", request.getAttribute("errorMessage"));
                break;
            case 500:
                model.addAttribute("errorMessage", ErrorPageConstants.STATUS_500);
                break;
            default:
                model.addAttribute("errorMessage", ErrorPageConstants.DEFAULT);
        }


        return "/error";
    }

}