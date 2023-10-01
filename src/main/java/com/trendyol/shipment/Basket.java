package com.trendyol.shipment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket {

    private static final int SHIPMENT_THRESHOLD = 3;

    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        if (isEmpty()) {
            return null;
        }

        ShipmentSize potentialUpgradeSize = findPotentialUpgradeSize();
        if (potentialUpgradeSize != null) {
            return potentialUpgradeSize;
        }

        return getLargestShipmentSize();
    }

    private boolean isEmpty() {
        return products == null || products.isEmpty();
    }

    private ShipmentSize findPotentialUpgradeSize() {
        Map<ShipmentSize, Long> productCountsByShipmentSize = countProductsByShipmentSize();

        for (Map.Entry<ShipmentSize, Long> entry : productCountsByShipmentSize.entrySet()) {
            if (entry.getValue() >= SHIPMENT_THRESHOLD) {
                return getUpgradedShipmentSize(entry.getKey());
            }
        }

        return null;
    }

    private Map<ShipmentSize, Long> countProductsByShipmentSize() {
        return products.stream()
                .map(Product::getSize)
                .collect(Collectors.groupingBy(shipmentSize -> shipmentSize, Collectors.counting()));
    }

    private ShipmentSize getUpgradedShipmentSize(ShipmentSize currentSize) {
        if (currentSize == ShipmentSize.X_LARGE) {
            return ShipmentSize.X_LARGE;
        }
        return ShipmentSize.values()[currentSize.ordinal() + 1];
    }

    private ShipmentSize getLargestShipmentSize() {
        return Collections.max(countProductsByShipmentSize().keySet());
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
