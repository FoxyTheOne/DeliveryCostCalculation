package org.mycompany;

/**
 * Список для дополнительной стоимости в зависимости от размера груза
 */
public enum Size {
    SMALL(100),
    LARGE(200);

    private final double sizeCostRub;

    Size(double sizeCost) {
        this.sizeCostRub = sizeCost;
    }

    public double getSizeCostRub() {
        return sizeCostRub;
    }
}
