package org.joksin.multitenancy.api.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;

@Builder
@Serdeable
public record ProductDTO(Long id, String name, Integer count) {}
