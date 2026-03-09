package com.inventory.dto;

import com.inventory.enums.StockStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Full product representation returned in API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer quantity;
    private StockStatus stockStatus;
    private String stockStatusLabel;
    private String stockStatusColor;
    private LocalDateTime createdAt;
}
