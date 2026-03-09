package com.inventory.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Request body for PUT /api/products/{id}
 * All fields are optional to support partial updates.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters")
    private String name;

    @Size(min = 1, max = 100, message = "Category must be between 1 and 100 characters")
    private String category;

    @DecimalMin(value = "0.00", inclusive = true, message = "Price must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Price format invalid")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity must be 0 or greater")
    private Integer quantity;
}
