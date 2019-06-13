package com.andoverrobotics.inventory.sql;

import com.andoverrobotics.inventory.PartType;
import com.andoverrobotics.inventory.PersistenceGateway;
import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.query.SearchQuery;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.security.Identity;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Stream;

public class SqlPersistence implements PersistenceGateway {
  private Connection connection;
  private SqlProtocolAgent protocolAgent;

  private Collection<PartType> stateCache;

  public SqlPersistence(Driver sqlDriver, String url, String user, String password) throws SQLException {
    DriverManager.registerDriver(sqlDriver);
    connection = DriverManager.getConnection(url, user, password);
    protocolAgent = new PostgresProtocolAgent(connection);
    protocolAgent.initializeTables();
  }

  @Override
  public Stream<PartType> getCurrentState() {
    if (stateCache == null) {
      refreshStateCache();
    }
    return stateCache.stream();
  }

  @Override
  public Stream<PartType> filter(FilterQuery query) {
    if (!(query instanceof SearchQuery))
      throw new UnsupportedOperationException("FilterQuery that is not a SearchQuery");

    var search = (SearchQuery) query;

    return getCurrentState().filter(search::matches);
  }

  @Override
  public boolean change(Identity changer, Mutation mutation) {
    if (!(changer instanceof GoogleIdentity))
      throw new UnsupportedOperationException("Identity that is not a GoogleIdentity");

    mutation.apply(stateCache);

    return protocolAgent.mutate((GoogleIdentity) changer, mutation);
  }

  @Override
  public void rollback(AuditLogItem mostRecentActiveLogItem) throws IllegalArgumentException {
    stateCache = null;
    protocolAgent.rollback(mostRecentActiveLogItem);
  }

  @Override
  public Stream<AuditLogItem> auditLog() {
    return protocolAgent.auditLog();
  }

  @Override
  public boolean addToWhitelist(Identity adder, String newWhitelistItem) {
    return protocolAgent.addToWhitelist(adder, newWhitelistItem);
  }

  @Override
  public boolean removeFromWhitelist(Identity remover, String wasWhitelistItem) {
    return protocolAgent.removeFromWhitelist(remover, wasWhitelistItem);
  }

  @Override
  public Stream<String> whitelist() {
    return protocolAgent.whitelistEmails();
  }

  private void refreshStateCache() {
    var collection = new LinkedList<PartType>();

    protocolAgent.getSortedMutations()
        .forEachOrdered(it -> it.apply(collection));

    stateCache = collection;
  }
}
