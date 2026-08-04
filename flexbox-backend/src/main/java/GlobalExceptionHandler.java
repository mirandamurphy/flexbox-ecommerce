
import com.flexbox.backend.catalog.exception.ProductNotFoundException;
import com.flexbox.backend.catalog.exception.SubscriptionBoxNotFoundException;
import com.flexbox.backend.catalog.exception.SubscriptionBoxPriceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

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


}
