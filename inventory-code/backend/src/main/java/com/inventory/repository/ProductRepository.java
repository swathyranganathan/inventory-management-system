package com.inventory.repository;

import com.inventory.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ── Category filtering ─────────────────────────────────────────────
    List<Product> findByCategory(String category);

    Page<Product> findByCategory(String category, Pageable pageable);

    // ── Stock level queries ────────────────────────────────────────────
    List<Product> findByQuantityLessThan(int threshold);

    List<Product> findByQuantityBetween(int min, int max);

    List<Product> findByQuantityGreaterThan(int threshold);

    // ── Search by name (case-insensitive) ─────────────────────────────
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByName(@Param("keyword") String keyword, Pageable pageable);

    // ── Distinct categories ────────────────────────────────────────────
    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category ASC")
    List<String> findDistinctCategories();

    // ── Analytics queries ──────────────────────────────────────────────
    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity < :threshold")
    long countByQuantityLessThan(@Param("threshold") int threshold);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity BETWEEN :min AND :max")
    long countByQuantityBetween(@Param("min") int min, @Param("max") int max);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity > :threshold")
    long countByQuantityGreaterThan(@Param("threshold") int threshold);

    @Query("SELECT SUM(p.price * p.quantity) FROM Product p")
    java.math.BigDecimal calculateTotalInventoryValue();

    @Query("SELECT p.category, COUNT(p), SUM(p.price * p.quantity) " +
           "FROM Product p GROUP BY p.category ORDER BY SUM(p.price * p.quantity) DESC")
    List<Object[]> getCategoryBreakdown();

    @Query("SELECT p FROM Product p ORDER BY (p.price * p.quantity) DESC")
    List<Product> findTopByValue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.createdAt >= :since ORDER BY p.createdAt DESC")
    List<Product> findRecentlyAdded(@Param("since") java.time.LocalDateTime since);
}
