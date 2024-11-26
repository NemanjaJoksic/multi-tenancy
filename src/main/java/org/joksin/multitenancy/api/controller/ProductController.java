package org.joksin.multitenancy.api.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import lombok.AllArgsConstructor;
import org.joksin.multitenancy.api.dto.ProductDTO;
import org.joksin.multitenancy.api.dto.request.CreateProductRequestDTO;
import org.joksin.multitenancy.common.TenantContext;
import org.joksin.multitenancy.database.entity.ProductEntity;
import org.joksin.multitenancy.database.repository.ProductJpaRepository;
import org.joksin.multitenancy.database.util.TenantAwareQueryExecutor;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

  private final ProductJpaRepository productJpaRepository;

  private final TenantAwareQueryExecutor tenantAwareQueryExecutor;
  private final TenantContext tenantContext;

  @Get("/api/products")
  public List<ProductDTO> findAll(@Header("Tenant") String tenant) {
    tenantContext.initialize(tenant);

    return tenantAwareQueryExecutor.executeInTransactionReadOnly(
        () -> productJpaRepository.findAll().stream().map(this::toDTO).toList());
  }

  @Post("/api/products")
  @Status(HttpStatus.CREATED)
  public ProductDTO create(
      @Header("Tenant") String tenant, @Body CreateProductRequestDTO createProductRequestDto) {
    tenantContext.initialize(tenant);

    return tenantAwareQueryExecutor.executeInTransaction(
        () -> {
          var createdProductEntity =
              productJpaRepository.save(
                  ProductEntity.builder()
                      .name(createProductRequestDto.name())
                      .count(createProductRequestDto.count())
                      .build());

          return toDTO(createdProductEntity);
        });
  }

  private ProductDTO toDTO(ProductEntity productEntity) {
    return ProductDTO.builder()
        .id(productEntity.getId())
        .name(productEntity.getName())
        .count(productEntity.getCount())
        .build();
  }
}
