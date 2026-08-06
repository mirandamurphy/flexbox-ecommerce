package com.flexbox.backend.config;

import com.flexbox.backend.cart.InsufficientStockException;
import com.flexbox.backend.catalog.exception.ProductNotFoundException;
import com.flexbox.backend.catalog.exception.SubscriptionBoxNotFoundException;
import com.flexbox.backend.catalog.exception.SubscriptionBoxPriceNotFoundException;
import com.flexbox.backend.order.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /*
    Logging strategy:
    Domain Errors (4xx): Warn
    System Errors (5xx): Error
     */


    /*
    Exceptions for Product Catalog
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(ProductNotFoundException e) {
        log.warn("Product lookup failed: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        problem.setTitle("Product Not Found");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @ExceptionHandler(SubscriptionBoxNotFoundException.class)
    public ProblemDetail handleSubscriptionBoxNotFound(SubscriptionBoxNotFoundException e) {
        log.warn("Subscription box lookup failed: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        problem.setTitle("Subscription Box Not Found");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @ExceptionHandler(SubscriptionBoxPriceNotFoundException.class)
    public ProblemDetail handleSubscriptionBoxPriceNotFound(SubscriptionBoxPriceNotFoundException e) {
        log.warn("Subscription box price lookup failed: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        problem.setTitle("Subscription Box Price Not Found");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    /*
    Exceptions for Cart and Checkout
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ProblemDetail handleInsufficientStock(InsufficientStockException e) {
        log.warn("Insufficient stock: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage()
        );

        problem.setTitle("Insufficient Stock");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleOrderNotFound(OrderNotFoundException e) {
        log.warn("Order lookup failed: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        problem.setTitle("Order Not Found");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
        log.warn("Invalid request: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        problem.setTitle("Not Found");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException e) {
        log.warn("Invalid state for request: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage()
        );

        problem.setTitle("Invalid State");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }


}
