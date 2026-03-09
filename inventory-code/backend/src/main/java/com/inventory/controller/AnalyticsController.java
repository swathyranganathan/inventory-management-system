package com.inventory.controller;

import com.inventory.dto.AnalyticsSummaryDTO;
import com.inventory.dto.ProductDTO;
import com.inventory.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Inventory analytics and reporting")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // ── GET /api/analytics/summary ─────────────────────────────────────
    @GetMapping("/summary")
    @Operation(summary = "Dashboard KPIs: totals, status counts, category breakdown")
    public ResponseEntity<AnalyticsSummaryDTO> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummary());
    }

    // ── GET /api/analytics/categories ─────────────────────────────────
    @GetMapping("/categories")
    @Operation(summary = "Product count and inventory value per category")
    public ResponseEntity<List<AnalyticsSummaryDTO.CategoryStat>> getCategoryBreakdown() {
        return ResponseEntity.ok(analyticsService.getCategoryBreakdown());
    }

    // ── GET /api/analytics/stock-distribution ─────────────────────────
    @GetMapping("/stock-distribution")
    @Operation(summary = "Count of products in each stock status level")
    public ResponseEntity<Map<String, Long>> getStockDistribution() {
        return ResponseEntity.ok(analyticsService.getStockDistribution());
    }

    // ── GET /api/analytics/top-valued?limit=10 ────────────────────────
    @GetMapping("/top-valued")
    @Operation(summary = "Top N products by total value (price × quantity)")
    public ResponseEntity<List<ProductDTO>> getTopValued(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getTopValuedProducts(limit));
    }

    // ── GET /api/analytics/recent?days=7 ──────────────────────────────
    @GetMapping("/recent")
    @Operation(summary = "Products added in the last N days")
    public ResponseEntity<List<ProductDTO>> getRecentlyAdded(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(analyticsService.getRecentlyAdded(days));
    }
}
