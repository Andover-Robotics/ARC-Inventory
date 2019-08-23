package com.andoverrobotics.inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

// Manages all the public, data-free pages (do not require log in or show inventory data)
@Controller
public class PublicPageController {
    @GetMapping("/")
    public String index(Model model, @CookieValue(value = "session", defaultValue = "") String encryptedEmail) {
        if (Validators.isLoggedIn(encryptedEmail))
            model.addAttribute("logged_in", true);
        return "index"; // Render index.template
    }

    @GetMapping("/about")
    public String about(Model model, @CookieValue(value = "session", defaultValue = "") String encryptedEmail) {
        if (Validators.isLoggedIn(encryptedEmail))
            model.addAttribute("logged_in", true);
        return "about"; // Render index.template
    }

    @GetMapping("/login")
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model, @CookieValue(value = "session", defaultValue = "") String encryptedEmail, @RequestParam(value = "redirect", required=false) String redirect) {
//        if (Validators.isLoggedIn(encryptedEmail))
//            return "redirect:" + ((redirect == null) ? "/" : redirect);
        return "login"; // Render login.template
    }
}
