package com.inventory.service;

import com.inventory.dto.AnalyticsSummaryDTO;
import com.inventory.dto.ProductDTO;
import com.inventory.mapper.ProductMapper;
import com.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AnalyticsService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Full dashboard summary: KPIs + category breakdown.
     */
    public AnalyticsSummaryDTO getSummary() {
        long total     = productRepository.count();
        long lowCount  = productRepository.countByQuantityLessThan(10);
        long medCount  = productRepository.countByQuantityBetween(10, 20);
        long normCount = productRepository.countByQuantityGreaterThan(20);

        BigDecimal totalValue = productRepository.calculateTotalInventoryValue();
        if (totalValue == null) totalValue = BigDecimal.ZERO;

        List<String> categories = productRepository.findDistinctCategories();
        List<Object[]> rawStats = productRepository.getCategoryBreakdown();

        List<AnalyticsSummaryDTO.CategoryStat> categoryStats = rawStats.stream()
                .map(row -> AnalyticsSummaryDTO.CategoryStat.builder()
                        .category((String) row[0])
                        .productCount(((Number) row[1]).longValue())
                        .totalValue(row[2] != null ? (BigDecimal) row[2] : BigDecimal.ZERO)
                        .build())
                .collect(Collectors.toList());

        return AnalyticsSummaryDTO.builder()
                .totalProducts(total)
                .totalCategories(categories.size())
                .totalInventoryValue(totalValue)
                .lowStockCount(lowCount)
                .mediumStockCount(medCount)
                .normalStockCount(normCount)
                .categoryStats(categoryStats)
                .build();
    }

    /**
     * Category breakdown for charts.
     */
    public List<AnalyticsSummaryDTO.CategoryStat> getCategoryBreakdown() {
        return productRepository.getCategoryBreakdown().stream()
                .map(row -> AnalyticsSummaryDTO.CategoryStat.builder()
                        .category((String) row[0])
                        .productCount(((Number) row[1]).longValue())
                        .totalValue(row[2] != null ? (BigDecimal) row[2] : BigDecimal.ZERO)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Top N most valuable products (price * quantity).
     */
    public List<ProductDTO> getTopValuedProducts(int limit) {
        return productMapper.toDTOList(
                productRepository.findTopByValue(PageRequest.of(0, limit))
        );
    }

    /**
     * Products added in the last N days.
     */
    public List<ProductDTO> getRecentlyAdded(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return productMapper.toDTOList(productRepository.findRecentlyAdded(since));
    }

    /**
     * Stock distribution: counts per status.
     */
    public java.util.Map<String, Long> getStockDistribution() {
        return java.util.Map.of(
                "LOW",    productRepository.countByQuantityLessThan(10),
                "MEDIUM", productRepository.countByQuantityBetween(10, 20),
                "NORMAL", productRepository.countByQuantityGreaterThan(20)
        );
    }
}
