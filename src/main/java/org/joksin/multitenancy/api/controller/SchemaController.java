package org.joksin.multitenancy.api.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.Map;

@Controller
@AllArgsConstructor
public class SchemaController {

  private final EntityManager entityManager;

  @Get("/api/schemas/current")
  @Transactional
  public Map<String, String> getCurrentSchema() {
    var result = entityManager.createNativeQuery("select current_schema;").getResultList();
    return Map.of("currentSchema", result.get(0).toString());
  }
}
