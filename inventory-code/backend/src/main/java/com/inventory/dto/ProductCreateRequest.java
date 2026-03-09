package com.inventory.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Request body for POST /api/products
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(min = 1, max = 100, message = "Category must be between 1 and 100 characters")
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Price must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Price format invalid (max 10 digits, 2 decimal places)")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be 0 or greater")
    private Integer quantity;
}
