package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {

    @Autowired
    private ReviewReactiveRepository reviewReactiveRepository;

    public Mono<ServerResponse> addReview(ServerRequest request) {

        //extracting the request body
        return request.bodyToMono(Review.class)
                //saving the Review
                .flatMap(reviewReactiveRepository::save)
                //Defines the status of ServerResponse and provides bodyvalue;
                        .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

}
