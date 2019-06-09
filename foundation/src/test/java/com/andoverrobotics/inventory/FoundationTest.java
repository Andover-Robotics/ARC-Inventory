package com.andoverrobotics.inventory;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.security.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FoundationTest {
  private PersistenceGateway persistence = mock(PersistenceGateway.class);
  private GoogleAccountVerifier accountVerifier = mock(GoogleAccountVerifier.class);
  private FoundationGateway foundation = new Foundation(persistence, accountVerifier);

  private static Identity publicIdentity = mock(Identity.class),
      whitelistedIdentity = new GoogleIdentity("120123012", "whitelisted@gmail.com", "The Whitelisted", ""),
      adminIdentity = new GoogleIdentity("1012031923", "any@andoverrobotics.com", "The Admin",
          "", "andoverrobotics.com");
  private static Mutation mockMutation = mock(Mutation.class);

  private static final AuditLogItem[] kLogItems = {
      new AuditLogItemImpl(whitelistedIdentity, mockMutation,
          LocalDateTime.of(2019, 6, 10, 14, 12, 1)),
      new AuditLogItemImpl(whitelistedIdentity, mockMutation,
          LocalDateTime.of(2019, 6, 12, 14, 12, 1)),
      new AuditLogItemImpl(whitelistedIdentity, mockMutation,
          LocalDateTime.of(2019, 6, 12, 18, 12, 1)),
      new AuditLogItemImpl(whitelistedIdentity, mockMutation,
          LocalDateTime.of(2019, 6, 13, 1, 12, 1)),
  };

  @Before
  public void setUp() {
    reset(persistence, accountVerifier);
    when(persistence.whitelist()).thenReturn(Stream.of("whitelisted@gmail.com"));
    when(persistence.auditLog()).thenReturn(Stream.of(kLogItems));
  }

  @Test
  public void passFilterQueriesToPersistence() {
    var filter = mock(FilterQuery.class);
    when(persistence.filter(filter)).thenReturn(Stream.empty());

    var returnVal = foundation.filter(null, filter);

    verify(persistence).filter(filter);
    assertEquals(0, returnVal.count());
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectNullFilterQueries() {
    foundation.filter(null, null);
  }

  @Test
  public void useVerifierToMakeIdentity() throws GeneralSecurityException, IOException {
    var googleIdentity = mock(GoogleIdentity.class);
    when(accountVerifier.verifyIdToken("my id token")).thenReturn(googleIdentity);

    var identity = foundation.identify("my id token");

    assertEquals(googleIdentity, identity);
  }

  @Test(expected = IllegalArgumentException.class)
  public void throwsIllegalArgumentForInvalidIdToken() throws GeneralSecurityException, IOException {
    when(accountVerifier.verifyIdToken("bad token")).thenThrow(new IllegalArgumentException());

    foundation.identify("bad token");
  }

  @Test
  public void andoverRoboticsEmailMeansAdmin() throws GeneralSecurityException, IOException {
    var admin = new GoogleIdentity("00010001001", "admin@andoverrobotics.com", "admin", "",
        "andoverrobotics.com");

    var level = foundation.permissionLevelOf(admin);

    assertEquals(PermissionLevel.ADMIN, level);
  }

  @Test
  public void whitelistedEmailMeansEdit() {
    when(persistence.whitelist()).thenReturn(Stream.of("whitelisted@email.com"));
    var whitelisted = new GoogleIdentity("0010010010", "whitelisted@email.com", "whitelisted", "");

    var level = foundation.permissionLevelOf(whitelisted);

    assertEquals(PermissionLevel.WHITELISTED, level);
  }

  @Test
  public void nonWhitelistedEmailMeansPublic() {
    when(persistence.whitelist()).thenReturn(Stream.empty());
    var nonWhitelisted = new GoogleIdentity("0100120120", "non.whitelisted@gmail.com", "Not Whitelisted", "");

    var level = foundation.permissionLevelOf(nonWhitelisted);

    assertEquals(PermissionLevel.PUBLIC, level);
  }

  @Test
  public void nonGoogleMeansPublic() {
    var nonGoogle = mock(Identity.class);

    var level = foundation.permissionLevelOf(nonGoogle);

    assertEquals(PermissionLevel.PUBLIC, level);
  }

  @Test
  public void nullMeansPublic() {
    assertEquals(PermissionLevel.PUBLIC, foundation.permissionLevelOf(null));
  }

  @Test(expected = UnauthorizedException.class)
  public void publicCannotAddToWhitelist() {
    foundation.addEmailToWhitelist(publicIdentity, "new-whitelist@gmail.com");
  }

  @Test(expected = UnauthorizedException.class)
  public void whitelistedEditorsCannotAddToWhitelist() {
    foundation.addEmailToWhitelist(whitelistedIdentity, "new.whitelisted@gmail.com");
  }

  @Test
  public void adminsCanAddToWhitelist() {
    when(persistence.addToWhitelist(any(), any())).thenReturn(true);

    assertTrue(foundation.addEmailToWhitelist(adminIdentity, "new.whitelisted@gmail.com"));

    verify(persistence).addToWhitelist(adminIdentity, "new.whitelisted@gmail.com");
  }

  @Test(expected = UnauthorizedException.class)
  public void publicCannotRemoveFromWhitelist() {
    foundation.removeEmailFromWhitelist(publicIdentity, "whitelisted@gmail.com");
  }

  @Test(expected = UnauthorizedException.class)
  public void whitelistedEditorsCannotRemoveFromWhitelist() {
    foundation.removeEmailFromWhitelist(whitelistedIdentity, "was.whitelisted@gmail.com");
  }

  @Test
  public void adminsCanRemoveFromWhitelist() {
    when(persistence.removeFromWhitelist(any(), any())).thenReturn(true);

    assertTrue(foundation.removeEmailFromWhitelist(adminIdentity, "was.whitelisted@gmail.com"));

    verify(persistence).removeFromWhitelist(adminIdentity, "was.whitelisted@gmail.com");
  }

  @Test(expected = UnauthorizedException.class)
  public void publicCannotReadWhitelist() {
    foundation.whitelist(publicIdentity);
  }

  @Test(expected = UnauthorizedException.class)
  public void whitelistedCannotReadWhitelist() {
    foundation.whitelist(whitelistedIdentity);
  }

  @Test
  public void adminCanReadWhitelist() {
    String[] expectedList = {
        "whitelisted@gmail.com"
    };

    var whitelist = foundation.whitelist(adminIdentity).toArray();

    assertArrayEquals(expectedList, whitelist);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectNullWhitelistEmails() {
    foundation.addEmailToWhitelist(adminIdentity, null);
    verifyZeroInteractions(persistence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectNullWhitelistRemovals() {
    foundation.removeEmailFromWhitelist(adminIdentity, null);
    verifyZeroInteractions(persistence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectNullMutations() {
    foundation.change(adminIdentity, null);
    verifyZeroInteractions(persistence);
  }

  @Test(expected = UnauthorizedException.class)
  public void publicCannotEdit() {
    foundation.change(publicIdentity, mock(Mutation.class));
    verifyZeroInteractions(persistence);
  }

  @Test
  public void mutationsPassedToPersistence() {
    var mutation = mock(Mutation.class);
    when(persistence.change(whitelistedIdentity, mutation)).thenReturn(true);

    var result = foundation.change(whitelistedIdentity, mutation);

    assertTrue(result);
    verify(persistence).change(whitelistedIdentity, mutation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectNullDateTimeForAuditLog() {
    foundation.auditLogSince(adminIdentity, null);
    verifyZeroInteractions(persistence);
  }

  @Test
  public void everyoneCanSeeAuditLog() {
    var startTime = LocalDateTime.of(2019, 1, 4, 10, 15, 0);

    var log = foundation.auditLogSince(null, startTime).toArray();

    assertArrayEquals(kLogItems, log);
    verify(persistence).auditLog();
  }

  @Test
  public void auditLogSinceDateBetweenLogItems() {
    var log = foundation.auditLogSince(adminIdentity,
        LocalDateTime.of(2019, 6, 11, 14, 12, 1)).toArray();

    for (int i = 0; i < log.length; i++) {
      assertEquals(kLogItems[i + 1], log[i]);
    }
  }

  @Test
  public void auditLogSinceDateOfLogItem() {
    var log = foundation.auditLogSince(adminIdentity,
        LocalDateTime.of(2019, 6, 12, 18, 12, 1)).toArray();

    assertEquals(2, log.length);
    for (int i = 0; i < log.length; i++) {
      assertEquals(kLogItems[i + 2], log[i]);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectNullRollbackLogItem() {
    foundation.rollback(adminIdentity, (AuditLogItem) null);
    verifyZeroInteractions(persistence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectNullRollbackDate() {
    foundation.rollback(adminIdentity, (LocalDateTime) null);
    verifyZeroInteractions(persistence);
  }

  @Test(expected = UnauthorizedException.class)
  public void publicCannotRollbackToLogItem() {
    foundation.rollback(publicIdentity, kLogItems[2]);
  }

  @Test(expected = UnauthorizedException.class)
  public void publicCannotRollbackToInstant() {
    foundation.rollback(publicIdentity, LocalDateTime.now());
  }

  @Test
  public void rollbackToLogItemCallsPersistence() {
    foundation.rollback(whitelistedIdentity, kLogItems[1]);
    verify(persistence).rollback(kLogItems[1]);
  }

  @Test
  public void rollbackToInstantSelectsLastLogItemBeforeInstant() {
    foundation.rollback(whitelistedIdentity, kLogItems[1].getTime().plusMinutes(2));
    verify(persistence).rollback(kLogItems[1]);
  }

  @Test
  public void rollbackToInstantSelectsLastLogItemEqualToInstant() {
    foundation.rollback(whitelistedIdentity, kLogItems[1].getTime());
    verify(persistence).rollback(kLogItems[1]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rollbackToInstantBeforeFirstItemThrowsException() {
    foundation.rollback(whitelistedIdentity, kLogItems[0].getTime().minusMinutes(1));
  }
}