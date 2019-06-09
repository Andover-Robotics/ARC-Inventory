package com.andoverrobotics.inventory.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleAccountVerifier {
  private static final String CLIENT_ID = "946689851710-hn96s5kuc0mhehvfcthjilcn73kju42u.apps.googleusercontent.com";

  private static GoogleIdTokenVerifier verifier;

  static {
    try {

      verifier = new GoogleIdTokenVerifier.Builder(
          GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
          .setAudience(Collections.singletonList(CLIENT_ID))
          .build();

    } catch (GeneralSecurityException | IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public GoogleIdentity verifyIdToken(String idToken) throws GeneralSecurityException, IOException, IllegalArgumentException {
    var token = verifier.verify(idToken);
    if (token == null) throw new IllegalArgumentException("invalid id token");

    var payload = token.getPayload();
    return new GoogleIdentity(
        payload.getSubject(),
        payload.getEmail(),
        (String) payload.get("name"),
        (String) payload.get("picture"),
        payload.getHostedDomain()
    );
  }
}
