package com.andoverrobotics.inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(Model model, @CookieValue(value = "session", defaultValue = "") String encryptedEmail) {
        System.out.println(WebApplication.encryptionManager.decrypt(encryptedEmail));
        if (Validators.isLoggedIn(encryptedEmail))
            model.addAttribute("logged_in", true);
        return "index"; // Render index.template
    }

    @GetMapping("/login")
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model, @CookieValue(value = "session", defaultValue = "") String encryptedEmail, @RequestParam(value = "redirect", required=false) String redirect) {
//        if (Validators.isLoggedIn(encryptedEmail))
//            return "redirect:" + ((redirect == null) ? "/" : redirect);
        return "login"; // Render login.template
    }
}
