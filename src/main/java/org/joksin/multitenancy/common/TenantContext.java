package org.joksin.multitenancy.common;

import io.micronaut.runtime.http.scope.RequestScope;

@RequestScope
public class TenantContext {

  private String tenantId;
  private boolean isInitialized = false;

  public void initialize(String tenantId) {
    checkIfNotInitialized();

    this.tenantId = tenantId;
    this.isInitialized = true;
  }

  public String getTenantId() {
    checkIfInitialized();

    return this.tenantId;
  }

  private void checkIfInitialized() {
    if (!this.isInitialized) {
      throw new RuntimeException("Tenant context is not initialized");
    }
  }

  private void checkIfNotInitialized() {
    if (this.isInitialized) {
      throw new RuntimeException(
          "Tenant context is already initialized. Cannot initialize it twice");
    }
  }
}
