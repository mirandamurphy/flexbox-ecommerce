package com.flexbox.backend.catalog.box.dto.subscriptionbox;

import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.product.dto.response.CategorySummaryResponse;

import java.util.List;

public record BoxDetailResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        BoxPriceResponse price,
        List<BoxProductResponse> products
) {

    public static BoxDetailResponse from (SubscriptionBox box,
                                          SubscriptionBoxPrice price,
                                          List<SubscriptionBoxProduct> products) {

        return new BoxDetailResponse(
                box.getId(),
                box.getName(),
                box.getDescription(),
                box.getImageFile(),
                BoxPriceResponse.from(price),
                products.stream()
                        .map(product -> BoxProductResponse.from(
                                product, CategorySummaryResponse.from(
                                        product.getProduct().getCategory())
                                        ))
                        .toList()
        );

    }
}
