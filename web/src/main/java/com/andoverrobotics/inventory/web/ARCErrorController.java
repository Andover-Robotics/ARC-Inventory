package com.andoverrobotics.inventory.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@Controller
public class ARCErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request, @CookieValue(value = "session", defaultValue = "") String encryptedEmail) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error_code", "404");
                model.addAttribute("error_description", "Page not found!");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error_code", "500");
                model.addAttribute("error_description", "Internal server error!");
            }
        }

        if (Validators.isLoggedIn(encryptedEmail))
            model.addAttribute("logged_in", true);

        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
