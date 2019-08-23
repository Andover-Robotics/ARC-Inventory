package com.andoverrobotics.inventory.web.browsing;

import com.andoverrobotics.inventory.PartType;
import com.andoverrobotics.inventory.web.GlobalConfig;
import com.andoverrobotics.inventory.web.Validators;
import com.andoverrobotics.inventory.web.WebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Controls the part browsing interface
@Controller
public class BrowseController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such browsing page found")
    class BrowsePageNotFoundException extends RuntimeException {
        public BrowsePageNotFoundException() {
            this(null);
        }

        public BrowsePageNotFoundException(String message) {
            super(message);
        }
    }

    @GetMapping("/browse")
    public String browseIndex(HttpServletResponse response) {
        try {
            response.sendRedirect("/browse/1");
        } catch (IOException e) {
            try {
                response.sendError(500);
            } catch (IOException e2) {
                return null;
            }
        }

        return null;
    }

    @GetMapping("/browse/{page}")
    public String index(Model model, @CookieValue(value = "session", defaultValue = "") String encryptedEmail, @PathVariable(value = "page") String page) {
        // First, check that the page is valid
        int pageIntValue;

        try {
            pageIntValue = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new BrowsePageNotFoundException();
        }

        // Is an integer, check that it's within the limits of the pages that will exist
        PartType[] parts = WebApplication.foundation.allParts().toArray(PartType[]::new); // Convert to array

        long numberOfPages = parts.length / GlobalConfig.BROWSING_MAX_ITEMS_PER_PAGE + 1;
        if (pageIntValue <= 0 || pageIntValue > numberOfPages)
            throw new BrowsePageNotFoundException();

        // It is a valid page number, set up the template rendering

        // Get the List of PartTypes to show on this page
        List<PartType> partsToShow = IntStream.range((pageIntValue - 1) * GlobalConfig.BROWSING_MAX_ITEMS_PER_PAGE, Math.min(pageIntValue * GlobalConfig.BROWSING_MAX_ITEMS_PER_PAGE, parts.length)).mapToObj(i -> parts[i]).collect(Collectors.toList());
        model.addAttribute("parts", partsToShow.stream().map(BrowsablePartType::new).collect(Collectors.toList()));
        model.addAttribute("num_pages", numberOfPages);
        model.addAttribute("current_page", pageIntValue);

        List<Field> fieldList = Arrays.asList(BrowsablePartType.class.getDeclaredFields());

        Collections.sort(fieldList, new FieldComparator()); // Sort in alphabetical order

        model.addAttribute("fields", fieldList);

        // Set logged_in
        if (Validators.isLoggedIn(encryptedEmail))
            model.addAttribute("logged_in", true);

        return "browse_page";
    }
}