package com.inventory.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dashboard analytics summary returned by GET /api/analytics/summary
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsSummaryDTO {

    // ── KPI Counts ─────────────────────────────────────────────────────
    private long totalProducts;
    private long totalCategories;
    private BigDecimal totalInventoryValue;

    // ── Stock Status Breakdown ─────────────────────────────────────────
    private long lowStockCount;     // quantity < 10
    private long mediumStockCount;  // 10 <= quantity <= 20
    private long normalStockCount;  // quantity > 20

    // ── Category Breakdown ─────────────────────────────────────────────
    private List<CategoryStat> categoryStats;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryStat {
        private String category;
        private long productCount;
        private BigDecimal totalValue;
    }
}
