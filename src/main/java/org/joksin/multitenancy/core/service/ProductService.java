package org.joksin.multitenancy.core.service;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.joksin.multitenancy.core.dto.ProductDTO;
import org.joksin.multitenancy.core.dto.request.CreateProductRequestDTO;
import org.joksin.multitenancy.core.TenantContext;
import org.joksin.multitenancy.core.dto.request.UpdateProductRequestDTO;
import org.joksin.multitenancy.database.entity.ProductEntity;
import org.joksin.multitenancy.database.repository.ProductJpaRepository;
import org.joksin.multitenancy.database.util.SchemaAwareQueryExecutor;

import java.util.List;

@Singleton
@AllArgsConstructor
public class ProductService {

  private final ProductJpaRepository productJpaRepository;

  private final SchemaAwareQueryExecutor schemaAwareQueryExecutor;
  private final TenantContext tenantContext;

  public List<ProductDTO> findAll() {
    return schemaAwareQueryExecutor.executeInTransactionReadOnly(
        tenantContext.getTenantId(),
        () -> productJpaRepository.findAll().stream().map(this::toDTO).toList());
  }

  public ProductDTO create(CreateProductRequestDTO createProductRequestDto) {
    return schemaAwareQueryExecutor.executeInTransaction(
        tenantContext.getTenantId(),
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

  public ProductDTO update(UpdateProductRequestDTO updateProductRequestDto) {
    return schemaAwareQueryExecutor.executeInTransaction(
        tenantContext.getTenantId(),
        () -> {
          var productEntity =
              productJpaRepository.findById(updateProductRequestDto.id()).orElseThrow();

          productEntity.setName(updateProductRequestDto.name());
          productEntity.setCount(updateProductRequestDto.count());

          var updatedProductEntity = productJpaRepository.save(productEntity);
          return toDTO(updatedProductEntity);
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
