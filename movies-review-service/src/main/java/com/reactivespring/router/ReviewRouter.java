package com.reactivespring.router;

import com.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    public static final String REVIEWS_ENDPOINT = "/reviews";

    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler reviewHandler){

        return route()
                .POST(REVIEWS_ENDPOINT, reviewHandler::addReview)
                .build();
    }


}
