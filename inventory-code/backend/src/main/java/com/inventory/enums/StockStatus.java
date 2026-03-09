package com.inventory.enums;

/**
 * Represents the stock health of a product based on its quantity.
 *
 * LOW    → quantity < 10   — requires immediate restock
 * MEDIUM → 10 ≤ qty ≤ 20  — monitor closely
 * NORMAL → quantity > 20   — healthy stock level
 */
public enum StockStatus {

    LOW("Low Stock", "#C0392B"),
    MEDIUM("Medium Stock", "#E67E22"),
    NORMAL("Normal", "#27AE60");

    private final String label;
    private final String colorHex;

    StockStatus(String label, String colorHex) {
        this.label = label;
        this.colorHex = colorHex;
    }

    public String getLabel() { return label; }
    public String getColorHex() { return colorHex; }

    /**
     * Derives StockStatus from a quantity value.
     */
    public static StockStatus from(int quantity) {
        if (quantity < 10)  return LOW;
        if (quantity <= 20) return MEDIUM;
        return NORMAL;
    }
}
