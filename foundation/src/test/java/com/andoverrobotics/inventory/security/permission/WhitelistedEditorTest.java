package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;
import org.junit.Test;

import static com.andoverrobotics.inventory.security.Action.*;
import static org.junit.Assert.*;

public class WhitelistedEditorTest {
  private WhitelistedEditor editor = new WhitelistedEditor();

  @Test
  public void canReadAndEdit() {
    assertTrue(editor.canPerform(READ_INVENTORY));
    assertTrue(editor.canPerform(EDIT_INVENTORY));
  }

  @Test
  public void cannotManage() {
    assertFalse(editor.canPerform(MANAGE));
  }
}