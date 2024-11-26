package org.joksin.multitenancy.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "product")
public class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
  @SequenceGenerator(name = "product_id_seq", allocationSize = 1)
  private Long id;

  @Setter private String name;

  @Setter private Integer count;
}
