package com.inventory.controller;

import com.inventory.dto.ProductCreateRequest;
import com.inventory.dto.ProductDTO;
import com.inventory.dto.ProductUpdateRequest;
import com.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product CRUD and stock operations")
public class ProductController {

    private final ProductService productService;

    // ── GET /api/products ──────────────────────────────────────────────
    @GetMapping
    @Operation(summary = "Get all products (paginated)")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    // ── GET /api/products/{id} ─────────────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ── POST /api/products ─────────────────────────────────────────────
    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        ProductDTO created = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ── PUT /api/products/{id} ─────────────────────────────────────────
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // ── PATCH /api/products/{id}/quantity ──────────────────────────────
    @PatchMapping("/{id}/quantity")
    @Operation(summary = "Update product quantity only")
    public ResponseEntity<ProductDTO> updateQuantity(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        Integer qty = body.get("quantity");
        if (qty == null || qty < 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.updateQuantity(id, qty));
    }

    // ── DELETE /api/products/{id} ──────────────────────────────────────
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ── GET /api/products/low-stock ────────────────────────────────────
    @GetMapping("/low-stock")
    @Operation(summary = "Get all low-stock products (quantity < 10)")
    public ResponseEntity<List<ProductDTO>> getLowStock() {
        return ResponseEntity.ok(productService.getLowStockProducts());
    }

    // ── GET /api/products/category/{category} ──────────────────────────
    @GetMapping("/category/{category}")
    @Operation(summary = "Filter products by category")
    public ResponseEntity<List<ProductDTO>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // ── GET /api/products/search?q=keyword ────────────────────────────
    @GetMapping("/search")
    @Operation(summary = "Search products by name keyword")
    public ResponseEntity<List<ProductDTO>> search(
            @RequestParam(name = "q") String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    // ── GET /api/products/categories ──────────────────────────────────
    @GetMapping("/categories")
    @Operation(summary = "Get all distinct categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }
}
