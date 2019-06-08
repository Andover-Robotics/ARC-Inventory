package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;

public class WhitelistedEditor extends AuthorizedActionsPermissionLevel {
  WhitelistedEditor() {
    super(Action.READ_INVENTORY, Action.EDIT_INVENTORY);
  }
}
