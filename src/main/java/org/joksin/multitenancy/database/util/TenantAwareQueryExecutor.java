package org.joksin.multitenancy.database.util;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@Singleton
@AllArgsConstructor
public class TenantAwareQueryExecutor {

  private EntityManager entityManager;

  @Transactional
  public <T> T executeInTransaction(String tenant, Supplier<T> transaction) {
    return executeInTransaction(tenant, transaction, true);
  }

  @Transactional(readOnly = true)
  public <T> T executeInTransactionReadOnly(String tenant, Supplier<T> transaction) {
    return executeInTransaction(tenant, transaction, false);
  }

  private <T> T executeInTransaction(String tenant, Supplier<T> transaction, boolean shouldFlush) {
    try {
      setSchema(tenant);
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
