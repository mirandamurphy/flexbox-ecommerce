package com.flexbox.backend.config;

import com.flexbox.backend.auth.InvalidCredentialsException;
import com.flexbox.backend.cart.InsufficientStockException;
import com.flexbox.backend.cart.InvalidQuantityException;
import com.flexbox.backend.common.exception.BusinessRuleException;
import com.flexbox.backend.common.exception.ResourceAlreadyExistsException;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import com.flexbox.backend.order.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.time.Instant;
import java.time.OffsetDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ProblemDetail handleResourceConflict(ResourceAlreadyExistsException e){

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage());

        problem.setTitle("Resource Already Exists");
        problem.setProperty("timestamp", OffsetDateTime.now());
        return problem;

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException e){

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage());

        problem.setTitle("Resource Not Found");
        problem.setProperty("timestamp", OffsetDateTime.now());
        return problem;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException e){

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage());

        problem.setTitle("Bad Request");
        problem.setProperty("timestamp", OffsetDateTime.now());
        return problem;

    }


    /*
   Exceptions for Auth
    */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException e) {
        log.warn("Login failed: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );

        problem.setTitle("Invalid Credentials");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    /*
    Exceptions for Cart and Checkout
     */
    @ExceptionHandler(InvalidQuantityException.class)
    public ProblemDetail handleInvalidQuantity(InvalidQuantityException e) {
        log.warn("Invalid quantity: {}", e.getMessage());

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        problem.setTitle("Invalid Quantity");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

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
