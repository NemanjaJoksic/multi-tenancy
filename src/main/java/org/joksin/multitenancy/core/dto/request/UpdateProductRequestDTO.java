package org.joksin.multitenancy.core.dto.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.With;

@Serdeable
@Introspected
public record UpdateProductRequestDTO(@With Long id, String name, Integer count) {}
