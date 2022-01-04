package com.learnreactiveprogramming.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FluxAndMonoExamplesTest {

    FluxAndMonoExamples fluxAndMonoGeneratorService = new FluxAndMonoExamples();

    private Flux<User> user$$;

    /*
    -------------------------------------------------------------------------------------------------------------------
    UDEMY EXAMPLES
    -------------------------------------------------------------------------------------------------------------------
     */


    @Test
    public void namesFlux() {
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(stringFlux)
                .expectNext("olavo")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void namesFluxMap() {
        Flux<String> mappedStringFlux = fluxAndMonoGeneratorService.namesFlux_Map();

        StepVerifier.create(mappedStringFlux)
                .expectNext("OLAVO", "PIETRO", "LEO")
                .verifyComplete();
    }

    @Test
    public void namesFluxImmutability() {
        Flux<String> mappedStringFlux = fluxAndMonoGeneratorService.namesFlux_Immutability();

        StepVerifier.create(mappedStringFlux)
                .expectNext("olavo", "pietro", "leo")
                .verifyComplete();
    }

    @Test
    public void namesFluxFiltering() {
        Flux<String> filteredStringFlux = fluxAndMonoGeneratorService.namesFlux_Filtering(3);

        StepVerifier.create(filteredStringFlux)
                .expectNext("5-OLAVO", "6-PIETRO")
                .verifyComplete();
    }

    @Test
    public void namesFluxFlatmap() {
        Flux<String> flatmapStringFlux = fluxAndMonoGeneratorService.namesFlux_Flatmap(3);

        StepVerifier.create(flatmapStringFlux)
                .expectNext("O", "L", "A", "V", "O", "P", "I", "E", "T", "R", "O")
                .verifyComplete();
    }

    @Test
    public void nameMonoFlatmap() {
        Mono<List<String>> listMono = fluxAndMonoGeneratorService.nameMono_Flatmap();

        StepVerifier.create(listMono)
                .expectNext(List.of("O", "L", "A", "V", "O"))
                .verifyComplete();
    }

    @Test
    public void nameMonoFlatMapMany() {
        Flux<String> stringFlux = fluxAndMonoGeneratorService.nameMono_FlatmapMany();

        StepVerifier.create(stringFlux)
                .expectNext("O", "L", "A", "V", "O")
                .verifyComplete();
    }

    @Test
    public void namesFluxTransform() {
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_transform(5);

        StepVerifier.create(stringFlux)
                .expectNext("P", "I", "E", "T", "R", "O")
                .verifyComplete();
    }

    @Test
    public void namesFluxTransform_WhenEmpty_defaultIfEmpty() {
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_transform(6);

        StepVerifier.create(stringFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void namesFluxTransform_WhenEmpty_switchIfEmpty() {
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_transform_EmptyData(6);

        StepVerifier.create(stringFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    public void concatTest() {
        Flux<String> stringFlux = fluxAndMonoGeneratorService.flux_Concat();

        StepVerifier.create(stringFlux)
                .expectNext("a", "b", "c", "d", "e", "f")
                .verifyComplete();
    }

    @Test
    public void concatWithTest() {
        Flux<String> stringFlux = fluxAndMonoGeneratorService.flux_ConcatWith();

        StepVerifier.create(stringFlux)
                .expectNext("a", "b")
                .verifyComplete();
    }



    /*
    -------------------------------------------------------------------------------------------------------------------
    Tech.io EXAMPLES
    -------------------------------------------------------------------------------------------------------------------
    */
    @Getter
    @AllArgsConstructor
    private class User {
        private String username;
    }

    //StepVerifier
    // TODO Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then completes successfully.

    @Test
    public void expectFooBarComplete() {

        Flux<String> just = Flux.just("foo", "bar");

        StepVerifier
                .create(just)
                .expectNext("foo", "bar")
                .verifyComplete();
    }
    // TODO Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then a RuntimeException error.
//    @Test
//    void expectFooBarError() {
//        Flux<String> just = Flux.just("foo", "bar")
//                .doOnError(throwable -> {
//                    throw new RuntimeException();
//                });
//
//        StepVerifier
//                .create(just)
//                .expectNext("foo", "bar")
//                .verifyError(RuntimeException.class);

//    }

    // TODO Use StepVerifier to check that the flux parameter emits a User with "wwhite" username
    //  and another one with "jpinkman" then completes successfully.

    @BeforeEach
    public void setUp(){
        User jesse = new User("jesse");
        User walter = new User("walter");

        this.user$$ = Flux.just(jesse, walter).log();
    }

    @Test
    void expectWalterJesseComplete() {
        StepVerifier
                .create(user$$)
                .assertNext(user -> assertEquals(user.getUsername(), "jesse"))
                .assertNext(user -> assertEquals(user.getUsername(), "walter"))
                .verifyComplete();
    }

    //Request (back pressure)

    // TODO Create a StepVerifier that initially requests all values and expect 4 values to be received
    @Test
    public void requestAllExpectFour() {
        StepVerifier
                .create(user$$)
                .expectSubscription()
                .thenRequest(Long.MAX_VALUE)
                .expectNextCount(2)
                .verifyComplete();
    }

    // TODO Create a StepVerifier that initially requests 1 value and expects User.WALTER then requests
    //  another value and expects User.JESSE then stops verifying by cancelling the source
    @Test
    public void requestOneExpectJesseThenRequestOneExpectWalter() {
        StepVerifier
                .create(user$$)
                .thenRequest(1)
                .assertNext(user -> user.getUsername().equals("jesse"))
                .thenRequest(1)
                .assertNext(user -> user.getUsername().equals("walter"))
                .thenCancel()
                .log();
    }





}
