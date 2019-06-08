package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;

public class PublicReader extends AuthorizedActionsPermissionLevel {
  PublicReader() {
    super(Action.READ_INVENTORY);
  }
}
