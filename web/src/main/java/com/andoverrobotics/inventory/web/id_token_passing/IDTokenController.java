package com.andoverrobotics.inventory.web.id_token_passing;

import com.andoverrobotics.inventory.Foundation;
import com.andoverrobotics.inventory.security.Identity;
import com.andoverrobotics.inventory.security.PermissionLevel;
import com.andoverrobotics.inventory.web.WebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class IDTokenController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity persistPerson(@RequestBody POSTBody postBody) {
        if (postBody.getToken() != null) {

            try {
                Foundation foundation = WebApplication.foundation;
                Identity identity = foundation.identify(postBody.getToken());
                PermissionLevel accountPermissionLevel = foundation.permissionLevelOf(identity);

                if (accountPermissionLevel != PermissionLevel.PUBLIC && accountPermissionLevel != null)
                    return new ResponseEntity(HttpStatus.ACCEPTED);
            } catch (GeneralSecurityException | IOException e) {
                return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
