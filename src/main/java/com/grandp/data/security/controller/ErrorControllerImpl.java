package com.grandp.data.security.controller;

import com.grandp.data.constants.ErrorPageConstants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerImpl implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorControllerImpl.class);

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request, HttpServletResponse response, Exception e) {

        int requestCode = response.getStatus();

        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (throwable != null) {
            model.addAttribute("throwableMessage", throwable.getMessage());

            LOGGER.error(throwable.getMessage());
            throwable.printStackTrace();
        }

        switch (requestCode) {
            case 403:
                model.addAttribute("errorMessage", ErrorPageConstants.STATUS_403);
                break;
            case 404:
                model.addAttribute("errorMessage", ErrorPageConstants.STATUS_404);
                break;
            case  406:
                model.addAttribute("errorMessage", "406 Not Acceptable");
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
