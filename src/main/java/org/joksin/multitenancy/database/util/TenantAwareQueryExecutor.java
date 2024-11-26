package org.joksin.multitenancy.database.util;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.joksin.multitenancy.common.TenantContext;

import java.util.function.Supplier;

@Singleton
@AllArgsConstructor
public class TenantAwareQueryExecutor {

  private final EntityManager entityManager;
  private final TenantContext tenantContext;

  @Transactional
  public <T> T executeInTransaction(Supplier<T> transaction) {
    return executeInTransaction(transaction, true);
  }

  @Transactional(readOnly = true)
  public <T> T executeInTransactionReadOnly(Supplier<T> transaction) {
    return executeInTransaction(transaction, false);
  }

  private <T> T executeInTransaction(Supplier<T> transaction, boolean shouldFlush) {
    try {
      setSchema(tenantContext.getTenantId());
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
