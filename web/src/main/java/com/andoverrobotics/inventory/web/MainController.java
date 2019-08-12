package com.andoverrobotics.inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(Model model) {
        return "index"; // Render index.template
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login"; // Render login.template
    }
}
