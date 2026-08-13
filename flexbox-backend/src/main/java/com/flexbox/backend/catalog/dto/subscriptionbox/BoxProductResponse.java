package com.flexbox.backend.catalog.dto.subscriptionbox;

import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.product.response.CategorySummaryResponse;
import com.flexbox.backend.catalog.product.response.ProductResponse;


public record BoxProductResponse(
       ProductResponse product,
       Integer quantity
) {

    public static BoxProductResponse from (SubscriptionBoxProduct boxProduct, CategorySummaryResponse category) {
        return new BoxProductResponse(
               ProductResponse.from(boxProduct.getProduct(), category),
                boxProduct.getQuantity()
        );
    }
}
