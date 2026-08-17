package com.flexbox.backend.admin.catalog.service;

import com.flexbox.backend.admin.catalog.dto.response.AdminBoxPriceResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxProductResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxResponse;
import com.flexbox.backend.common.exception.BusinessRuleException;
import com.flexbox.backend.common.exception.ResourceAlreadyExistsException;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxProductRequest;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxPriceRequest;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxRequest;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxProductId;
import com.flexbox.backend.catalog.product.repository.ProductRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxPriceRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxProductRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminBoxService {

    private final ProductRepository productRepository;
    private final SubscriptionBoxRepository boxRepository;
    private final SubscriptionBoxProductRepository boxProductRepository;
    private final SubscriptionBoxPriceRepository boxPriceRepository;

    public AdminBoxService(ProductRepository productRepository, SubscriptionBoxRepository boxRepository, SubscriptionBoxProductRepository boxProductRepository, SubscriptionBoxPriceRepository boxPriceRepository) {
        this.productRepository = productRepository;
        this.boxRepository = boxRepository;
        this.boxProductRepository = boxProductRepository;
        this.boxPriceRepository = boxPriceRepository;
    }

    public AdminBoxResponse createBox(AdminCreateBoxRequest request) {

        if(boxRepository.existsByNameIgnoreCase(request.name())) {
            throw new ResourceAlreadyExistsException(
                    "A subscription box with the name '%s' already exists.".formatted(request.name()));
        }

        var box = new SubscriptionBox();
        box.setName(request.name());
        box.setDescription(request.description());
        box.setImageFile(request.imagePath());
        box.setAvailableUnits(request.availableUnits());
        box.setIsActive(request.isActive());

        var savedBox = boxRepository.save(box);

        return AdminBoxResponse.from(savedBox);
    }

    @Transactional
    public void deactivateBox(Long boxId) {

        if (!boxRepository.updateIsActiveById(false, boxId)) {
            throw new ResourceNotFoundException("Subscription box for id '%d' not found.".formatted(boxId));
        }
    }


    @Transactional
    public AdminBoxProductResponse createBoxProduct(Long boxId, AdminCreateBoxProductRequest request) {

        var box = boxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subscription box for id '%d' not found.".formatted(boxId)
                ));

        var product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product for id '%d' not found.".formatted(request.productId())
                ));

        if(!product.getIsActive()) {
            throw new BusinessRuleException(
                    "Cannot add an inactive product to a subscription box."
            );
        }

        var id = new SubscriptionBoxProductId(
                boxId,
                request.productId()
        );

        if(boxProductRepository.existsById(id)) {
            throw new ResourceAlreadyExistsException(
                    "Product with id '%d' is already included in this subscription box".formatted(request.productId())
            );
        }

        var boxProduct = new SubscriptionBoxProduct();
        boxProduct.setId(id);
        boxProduct.setSubscriptionBox(box);
        boxProduct.setProduct(product);
        boxProduct.setQuantity(request.quantity());

       var savedBoxProduct = boxProductRepository.save(boxProduct);

       return AdminBoxProductResponse.from(savedBoxProduct);
    }

    @Transactional
    public AdminBoxPriceResponse setBoxPrice(Long boxId, AdminCreateBoxPriceRequest request) {

        var box = boxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subscription box for id '%d' not found.".formatted(boxId)
                ));

        var price = new SubscriptionBoxPrice();
        price.setSubscriptionBox(box);
        price.setAmount(request.amount());
        price.setStartsAt(request.startsAt());
        price.setEndsAt(request.endsAt());

        // TODO: Optionally, Stipe price can be added here
        //price.setStripePriceId("price12345");

        var savedBoxPrice = boxPriceRepository.save(price);

        return AdminBoxPriceResponse.from(savedBoxPrice);
    }




}
