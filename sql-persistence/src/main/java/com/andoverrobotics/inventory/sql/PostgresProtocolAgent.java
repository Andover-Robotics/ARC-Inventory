package com.andoverrobotics.inventory.sql;

import com.andoverrobotics.inventory.PartType;
import com.andoverrobotics.inventory.mutations.Addition;
import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.security.Identity;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class PostgresProtocolAgent implements SqlProtocolAgent {
  private final Connection connection;

  public PostgresProtocolAgent(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void initializeTables() {
    try {
      var statement = connection.createStatement();
      URI file = Objects.requireNonNull(getClass().getClassLoader()
          .getResource("initialize-postgres.sql")).toURI();

      statement.execute(Files.readString(Paths.get(file)));
    } catch (Exception sql) {
      throw new RuntimeException(sql);
    }
  }

  @Override
  public Stream<Mutation> getSortedMutations() {
    try {
      var mutationSet = new LinkedList<Mutation>();

      addAdditions(mutationSet);
    } catch (SQLException | MalformedURLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }

  private void addAdditions(LinkedList<Mutation> mutationSet) throws SQLException, MalformedURLException {
    var statement = connection.createStatement();
    var additions = statement.executeQuery("SELECT * FROM inventory.addition_deletion\n" +
        "    INNER JOIN inventory.parts ON addition_deletion.uuid = parts.uuid\n" +
        "    INNER JOIN inventory.editors ON addition_deletion.adder = editors.email\n" +
        "WHERE type = 'add';");

    while (additions.next()) {
      mutationSet.add(new Addition(toPartType(additions)));
    }
  }
  private void addDeletions(LinkedList<Mutation> mutationSet) {
  }

  @Override
  public boolean mutate(GoogleIdentity mutator, Mutation mutation) {
    return false;
  }

  @Override
  public void rollback(AuditLogItem mostRecentActiveLogItem) throws IllegalArgumentException {

  }

  @Override
  public Stream<AuditLogItem> auditLog() {
    return null;
  }

  @Override
  public boolean addToWhitelist(Identity adder, String newWhitelistItem) {
    return false;
  }

  @Override
  public boolean removeFromWhitelist(Identity remover, String wasWhitelistItem) {
    return false;
  }

  @Override
  public Stream<String> whitelistEmails() {
    return null;
  }

  private boolean tableExists(String schemaTable) throws SQLException {
    var statement = connection.createStatement();
    return !statement.executeQuery("SELECT to_regclass('" + schemaTable + "')").wasNull();
  }

  private PartType toPartType(ResultSet resultSet) throws SQLException, MalformedURLException {
    return new PartType(
        resultSet.getString("name"),
        resultSet.getString("part_number"),
        resultSet.getString("brand"),
        resultSet.getString("category"),
        resultSet.getString("location"),
        resultSet.getString("team"),
        resultSet.getInt("quantity"),
        new URL(resultSet.getString("url")),
        new URL(resultSet.getString("image_url")),
        UUID.fromString(resultSet.getString("uuid")),
        (String[]) resultSet.getArray("keywords").getArray()
    );
  }
}
