package org.joksin.multitenancy.api.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import lombok.AllArgsConstructor;
import org.joksin.multitenancy.core.TenantContext;
import org.joksin.multitenancy.core.dto.ProductDTO;
import org.joksin.multitenancy.core.dto.request.CreateProductRequestDTO;
import org.joksin.multitenancy.core.dto.request.UpdateProductRequestDTO;
import org.joksin.multitenancy.core.service.ProductService;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

  private final ProductService productService;

  @Get("/api/products")
  @Status(HttpStatus.OK)
  public List<ProductDTO> findAll(@Header(value = "Tenant", defaultValue = "") String tenant) {
    return productService.findAll(new TenantContext(tenant));
  }

  @Post("/api/products")
  @Status(HttpStatus.CREATED)
  public ProductDTO create(
      @Header(value = "Tenant", defaultValue = "") String tenant,
      @Body CreateProductRequestDTO createProductRequestDto) {
    return productService.create(new TenantContext(tenant), createProductRequestDto);
  }

  @Put("/api/products/{id}")
  @Status(HttpStatus.OK)
  public ProductDTO update(
      @Header(value = "Tenant", defaultValue = "") String tenant,
      @PathVariable Long id,
      @Body UpdateProductRequestDTO updateProductRequestDto) {
    return productService.update(new TenantContext(tenant), updateProductRequestDto.withId(id));
  }
}
