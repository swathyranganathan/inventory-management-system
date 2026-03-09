package com.inventory.service;

import com.inventory.dto.ProductCreateRequest;
import com.inventory.dto.ProductDTO;
import com.inventory.dto.ProductUpdateRequest;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.mapper.ProductMapper;
import com.inventory.model.Product;
import com.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // ── READ ───────────────────────────────────────────────────────────

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.debug("Fetching all products, page: {}", pageable.getPageNumber());
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductDTO> dtos = productMapper.toDTOList(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    public ProductDTO getProductById(Long id) {
        log.debug("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDTO(product);
    }

    public List<ProductDTO> getLowStockProducts() {
        log.debug("Fetching low stock products (qty < 10)");
        return productMapper.toDTOList(productRepository.findByQuantityLessThan(10));
    }

    public List<ProductDTO> getMediumStockProducts() {
        return productMapper.toDTOList(productRepository.findByQuantityBetween(10, 20));
    }

    public List<ProductDTO> getProductsByCategory(String category) {
        log.debug("Fetching products by category: {}", category);
        return productMapper.toDTOList(productRepository.findByCategory(category));
    }

    public List<ProductDTO> searchProducts(String keyword) {
        log.debug("Searching products with keyword: {}", keyword);
        return productMapper.toDTOList(productRepository.searchByName(keyword));
    }

    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    // ── CREATE ─────────────────────────────────────────────────────────

    @Transactional
    public ProductDTO createProduct(ProductCreateRequest request) {
        log.info("Creating product: {}", request.getName());
        Product product = Product.builder()
                .name(request.getName().trim())
                .category(request.getCategory().trim())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
        Product saved = productRepository.save(product);
        saved.computeStockStatus();
        return productMapper.toDTO(saved);
    }

    // ── UPDATE ─────────────────────────────────────────────────────────

    @Transactional
    public ProductDTO updateProduct(Long id, ProductUpdateRequest request) {
        log.info("Updating product id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (request.getName() != null)     product.setName(request.getName().trim());
        if (request.getCategory() != null) product.setCategory(request.getCategory().trim());
        if (request.getPrice() != null)    product.setPrice(request.getPrice());
        if (request.getQuantity() != null) product.setQuantity(request.getQuantity());

        Product saved = productRepository.save(product);
        saved.computeStockStatus();
        return productMapper.toDTO(saved);
    }

    @Transactional
    public ProductDTO updateQuantity(Long id, int newQuantity) {
        log.info("Updating quantity for product id: {} to {}", id, newQuantity);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setQuantity(newQuantity);
        Product saved = productRepository.save(product);
        saved.computeStockStatus();
        return productMapper.toDTO(saved);
    }

    // ── DELETE ─────────────────────────────────────────────────────────

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
