package com.andoverrobotics.inventory.web.id_token_passing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IDTokenController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> persistPerson(@RequestBody POSTBody postBody) {
        System.out.println(postBody.getToken());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
