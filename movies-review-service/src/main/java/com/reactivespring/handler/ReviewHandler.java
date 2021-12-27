package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.ReviewDataException;
import com.reactivespring.repository.ReviewReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Slf4j
public class ReviewHandler {

    @Autowired
    private ReviewReactiveRepository reviewReactiveRepository;

    @Autowired
    private Validator validator;

    public Mono<ServerResponse> addReview(ServerRequest request) {

        //extracting the request body
        return request.bodyToMono(Review.class)
                .doOnNext(this::validate)
                //saving the Review
                .flatMap(reviewReactiveRepository::save)
                //Defines the status of ServerResponse and provides bodyvalue;
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    private void validate(Review review) {
        Set<ConstraintViolation<Review>> violations = validator.validate(review);
        log.info("violations: {}", violations);
        if (violations.size() > 0) {
            String errorMessage = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            throw new ReviewDataException(errorMessage);
        }
    }

}
