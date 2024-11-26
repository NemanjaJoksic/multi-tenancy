package org.joksin.multitenancy.api.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import lombok.AllArgsConstructor;
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
  public List<ProductDTO> findAll() {
    return productService.findAll();
  }

  @Post("/api/products")
  @Status(HttpStatus.CREATED)
  public ProductDTO create(@Body CreateProductRequestDTO createProductRequestDto) {
    return productService.create(createProductRequestDto);
  }

  @Put("/api/products/{id}")
  @Status(HttpStatus.OK)
  public ProductDTO update(
      @PathVariable Long id, @Body UpdateProductRequestDTO updateProductRequestDto) {
    return productService.update(updateProductRequestDto.withId(id));
  }
}
