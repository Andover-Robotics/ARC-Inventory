package com.andoverrobotics.inventory.web.login;

import com.andoverrobotics.inventory.Foundation;
import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.security.Identity;
import com.andoverrobotics.inventory.security.PermissionLevel;
import com.andoverrobotics.inventory.web.CacheManager;
import com.andoverrobotics.inventory.web.GlobalConfig;
import com.andoverrobotics.inventory.web.Validators;
import com.andoverrobotics.inventory.web.WebApplication;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity logInUser(@RequestBody LoginPOSTBody loginPostBody, HttpServletResponse response) {
        if (loginPostBody.getToken() != null) {

            try {
                Foundation foundation = WebApplication.foundation;
                Identity identity = foundation.identify(loginPostBody.getToken());
                PermissionLevel accountPermissionLevel = foundation.permissionLevelOf(identity);

                if (accountPermissionLevel != PermissionLevel.PUBLIC && accountPermissionLevel != null) {
                    // Account is valid, check that email has been passed correctly
                    String email = loginPostBody.getEmail();
                    if (email != null && email.length() > 0 && email.contains("@")) {
                        // Valid email, create cookie and add to cache HashMap
                        Cookie cookie = new Cookie("session", WebApplication.encryptionManager.encrypt(email));
                        cookie.setMaxAge(GlobalConfig.LOGIN_SESSION_TIME_IN_SEC);
                        cookie.setPath(GlobalConfig.SESSION_COOKIE_ENDPOINT);
                        cookie.setHttpOnly(true);

                        response.addCookie(cookie);

                        CacheManager.addNode(email, (GoogleIdentity) identity);

                        return new ResponseEntity(HttpStatus.ACCEPTED);
                    } else
                        return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } catch (GeneralSecurityException | IOException e) {
                return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    // Code that is run AFTER spring boot's default /login is done
    @RequestMapping("/logout-cookies")
    public String logOutUserInternal(@CookieValue(value = "session", defaultValue = "") String encryptedEmail, HttpServletResponse response) {
        // Delete the old cookie
        Cookie emptyCookie = new Cookie("session", null); // Not necessary, but saves bandwidth.
        emptyCookie.setPath(GlobalConfig.SESSION_COOKIE_ENDPOINT);
        emptyCookie.setMaxAge(0);
        response.addCookie(emptyCookie);

        try {
            response.sendRedirect("/logout-google");
        } catch (IOException e) {
            try {
                response.sendError(500);
            } catch (IOException e2) {
                return null;
            }
        }

        String decryptedEmail = WebApplication.encryptionManager.decrypt(encryptedEmail);

        if (Validators.isValidEmail(decryptedEmail))
            CacheManager.removeNode(decryptedEmail);

        return null;
    }

    // Code that is run AFTER cookies are cleared
    @RequestMapping("/logout-google")
    public ModelAndView logOutUserExternal(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logout"); // Render logout.template, which logs the user out of Google
        return modelAndView;
    }
}
