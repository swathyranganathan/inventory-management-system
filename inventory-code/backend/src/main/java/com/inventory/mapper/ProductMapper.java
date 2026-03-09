package com.inventory.mapper;

import com.inventory.dto.ProductDTO;
import com.inventory.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "stockStatusLabel", expression = "java(product.getStockStatus() != null ? product.getStockStatus().getLabel() : null)")
    @Mapping(target = "stockStatusColor", expression = "java(product.getStockStatus() != null ? product.getStockStatus().getColorHex() : null)")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOList(List<Product> products);
}
