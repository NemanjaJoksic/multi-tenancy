package org.joksin.multitenancy.core.dto.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public record CreateProductRequestDTO(String name, Integer count) {}
