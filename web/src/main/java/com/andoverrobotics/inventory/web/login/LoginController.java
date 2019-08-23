package com.andoverrobotics.inventory.web.login;

import com.andoverrobotics.inventory.Foundation;
import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.security.Identity;
import com.andoverrobotics.inventory.security.PermissionLevel;
import com.andoverrobotics.inventory.web.cache.CacheManager;
import com.andoverrobotics.inventory.web.GlobalConfig;
import com.andoverrobotics.inventory.web.Validators;
import com.andoverrobotics.inventory.web.WebApplication;
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
                    if (Validators.isValidEmail(email)) {
                        // Encrypt email
                        String encryptedEmail = WebApplication.encryptionManager.encrypt(email);

                        if (Validators.isLoggedIn(encryptedEmail)) {
                            // Already logged in, refresh session
                            CacheManager.getEntity(email).refreshSession();
                        } else {
                            // Not logged in, add to cache
                            CacheManager.addNode(email, (GoogleIdentity) identity);
                        }

                        // Create session cookie
                        Cookie cookie = new Cookie("session", encryptedEmail);
                        cookie.setMaxAge(GlobalConfig.LOGIN_SESSION_TIME_IN_SEC);
                        cookie.setPath(GlobalConfig.SESSION_COOKIE_ENDPOINT);
                        cookie.setHttpOnly(true);

                        response.addCookie(cookie);

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
        clearSessionCookie(response);

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

    private void clearSessionCookie(HttpServletResponse response) {
        Cookie emptyCookie = new Cookie("session", null); // Not necessary, but saves bandwidth.
        emptyCookie.setPath(GlobalConfig.SESSION_COOKIE_ENDPOINT);
        emptyCookie.setMaxAge(0);
        response.addCookie(emptyCookie);
    }
}
