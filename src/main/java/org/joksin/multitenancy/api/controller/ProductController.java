package org.joksin.multitenancy.api.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import lombok.AllArgsConstructor;
import org.joksin.multitenancy.core.dto.ProductDTO;
import org.joksin.multitenancy.core.dto.request.CreateProductRequestDTO;
import org.joksin.multitenancy.core.TenantContext;
import org.joksin.multitenancy.core.dto.request.UpdateProductRequestDTO;
import org.joksin.multitenancy.core.service.ProductService;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final TenantContext tenantContext;

  @Get("/api/products")
  @Status(HttpStatus.OK)
  public List<ProductDTO> findAll(@Header("Tenant") String tenant) {
    try {
      tenantContext.initialize(tenant);
      return productService.findAll();
    } finally {
      tenantContext.clear();
    }
  }

  @Post("/api/products")
  @Status(HttpStatus.CREATED)
  public ProductDTO create(
      @Header("Tenant") String tenant, @Body CreateProductRequestDTO createProductRequestDto) {
    try {
      tenantContext.initialize(tenant);
      return productService.create(createProductRequestDto);
    } finally {
      tenantContext.clear();
    }
  }

  @Put("/api/products/{id}")
  @Status(HttpStatus.OK)
  public ProductDTO update(
      @Header("Tenant") String tenant,
      @PathVariable Long id,
      @Body UpdateProductRequestDTO updateProductRequestDto) {
    try {
      tenantContext.initialize(tenant);
      return productService.update(updateProductRequestDto.withId(id));
    } finally {
      tenantContext.clear();
    }
  }
}
