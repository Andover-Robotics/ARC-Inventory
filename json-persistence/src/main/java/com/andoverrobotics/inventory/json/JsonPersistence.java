package com.andoverrobotics.inventory.json;

import com.andoverrobotics.inventory.PartType;
import com.andoverrobotics.inventory.PersistenceGateway;
import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.security.Identity;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

// {auditLog:[<mutation>],whitelist:[<item>]}
public class JsonPersistence implements PersistenceGateway {

  private JsonPersistenceDto dto;
  private Collection<PartType> currentState = new LinkedList<>();
  private File jsonFile;
  private Gson gson = new Gson();

  public JsonPersistence(String filename) throws IOException {
    jsonFile = new File(filename);
    if (!jsonFile.exists()) initializeFile();
    loadFromFile();
  }

  private void loadFromFile() throws FileNotFoundException {
    dto = gson.fromJson(new FileReader(jsonFile), JsonPersistenceDto.class);
    dto.auditLog.sort(Comparator.comparing(AuditLogItem::getTime));

    deduceCurrentState();
  }

  private void deduceCurrentState() {
    currentState.clear();
    for (var item : dto.auditLog) {
      item.getChange().apply(currentState);
    }
  }

  private void initializeFile() throws IOException {
    Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("template.json")),
      jsonFile.toPath());
  }

  @Override
  public Stream<PartType> getCurrentState() {
    return currentState.stream();
  }

  @Override
  public Stream<PartType> filter(FilterQuery query) {
    return getCurrentState()
        .filter(query::matches);
  }

  @Override
  public boolean change(Identity changer, Mutation mutation) {
    dto.auditLog.add(new JsonPersistenceDto.AuditLogItemDto((GoogleIdentity) changer, mutation, LocalDateTime.now()));
    mutation.apply(currentState);
    try {
      writeToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  private void writeToFile() throws IOException {
    Files.writeString(jsonFile.toPath(), gson.toJson(dto));
  }

  @Override
  public void rollback(AuditLogItem mostRecentActiveLogItem) throws IllegalArgumentException {
    int endIndex = -1;

    for (int i = dto.auditLog.size() - 1; i >= 0; i--) {
      if (dto.auditLog.get(i).equals(mostRecentActiveLogItem)) {
        endIndex = i;
        break;
      }
    }

    if (endIndex == -1) throw new IllegalArgumentException("unknown AuditLogItem");
    dto.auditLog = dto.auditLog.subList(0, endIndex + 1);

    deduceCurrentState();
  }

  @Override
  public Stream<AuditLogItem> auditLog() {
    // Mapping is required to make Java believe that the Stream of an implementing class is acceptable
    return dto.auditLog.stream().map(it -> it);
  }

  @Override
  public boolean addToWhitelist(Identity adder, String newWhitelistItem) {
    if (!(adder instanceof GoogleIdentity)) throw new UnsupportedOperationException("adder not GoogleIdentity");

    if (isWhitelisted(newWhitelistItem)) return false;

    var newItem = new JsonPersistenceDto.WhitelistItem();
    newItem.email = newWhitelistItem;
    newItem.adderEmail = ((GoogleIdentity) adder).email;
    newItem.timestamp = LocalDateTime.now();

    return dto.whitelist.add(newItem);
  }

  private boolean isWhitelisted(String email) {
    return dto.whitelist.stream().anyMatch(it -> it.email.equalsIgnoreCase(email));
  }

  @Override
  public boolean removeFromWhitelist(Identity remover, String wasWhitelistItem) {
    if (!(remover instanceof GoogleIdentity)) throw new UnsupportedOperationException("remover not GoogleIdentity");

    for (int i = 0; i < dto.whitelist.size(); i++) {
      if (dto.whitelist.get(i).email.equalsIgnoreCase(wasWhitelistItem)) {
        dto.whitelist.remove(i);
        return true;
      }
    }

    return false;
  }

  @Override
  public Stream<String> whitelist() {
    return dto.whitelist.stream().map(it -> it.email);
  }
}
