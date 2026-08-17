package com.flexbox.backend.admin.catalog.dto.response;

import com.flexbox.backend.catalog.product.model.ProductInventory;

public record AdminProductInventoryResponse(
        Integer inStock,
        Integer reserved) {

    public static AdminProductInventoryResponse from(ProductInventory inventory) {
        return new AdminProductInventoryResponse(
                inventory.getInStock(),
                inventory.getReserved()
        );
    }
}
