package com.flexbox.backend.catalog.box.dto;

import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.product.dto.response.CategorySummaryResponse;
import com.flexbox.backend.catalog.product.dto.response.ProductResponse;


public record BoxProductResponse(
       ProductResponse product,
       Integer quantity
) {
    public static BoxProductResponse from(
            SubscriptionBoxProduct boxProduct,
            CategorySummaryResponse category) {

        return new BoxProductResponse(
                ProductResponse.from(boxProduct.getProduct(), category),
                boxProduct.getQuantity()
        );
    }
}
