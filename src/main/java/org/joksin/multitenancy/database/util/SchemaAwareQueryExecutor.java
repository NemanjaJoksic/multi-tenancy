package org.joksin.multitenancy.database.util;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@Singleton
@AllArgsConstructor
public class SchemaAwareQueryExecutor {

  private final EntityManager entityManager;

  @Transactional
  public <T> T executeInTransaction(String tenantId, Supplier<T> transaction) {
    return executeInTransaction(tenantId, transaction, true);
  }

  @Transactional(readOnly = true)
  public <T> T executeInTransactionReadOnly(String tenantId, Supplier<T> transaction) {
    return executeInTransaction(tenantId, transaction, false);
  }

  private <T> T executeInTransaction(
      String tenantId, Supplier<T> transaction, boolean shouldFlush) {
    try {
      setSchema(tenantId);
      var result = transaction.get();

      if (shouldFlush) {
        entityManager.flush();
      }

      return result;
    } finally {
      setSchema("public");
    }
  }

  private void setSchema(String schema) {
    entityManager.createNativeQuery("SET SCHEMA '" + schema + "'").executeUpdate();
  }
}
