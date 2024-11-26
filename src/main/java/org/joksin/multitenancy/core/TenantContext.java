package org.joksin.multitenancy.core;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;

@Introspected
public record TenantContext(@NotBlank String tenantId) {
  public String getSchema() {
    return this.tenantId;
  }
}
