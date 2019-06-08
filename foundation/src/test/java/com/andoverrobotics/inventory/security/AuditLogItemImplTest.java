package com.andoverrobotics.inventory.security;

import com.andoverrobotics.inventory.mutations.Mutation;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AuditLogItemImplTest {
  private AuditLogItem ali;
  private Identity initiator = mock(Identity.class);
  private Mutation mutation = mock(Mutation.class);
  private LocalDateTime dateTime = LocalDateTime.now();

  @Test
  public void returnsValuesGivenInCtor() {
    ali = new AuditLogItemImpl(initiator, mutation, dateTime);

    assertEquals(initiator, ali.getInitiator());
    assertEquals(mutation, ali.getChange());
    assertEquals(dateTime, ali.getTime());
  }

  @Test
  public void constructorWithoutTimeSetsTimeToNow() {
    ali = new AuditLogItemImpl(initiator, mutation);

    assertTrue(ali.getTime().isEqual(LocalDateTime.now()));
  }
}