package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    public void namesFlux(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(stringFlux)
                .expectNext("olavo")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void namesFluxMap(){
        Flux<String> mappedStringFlux = fluxAndMonoGeneratorService.namesFlux_Map();

        StepVerifier.create(mappedStringFlux)
                .expectNext("OLAVO", "PIETRO", "LEO")
                .verifyComplete();
    }

    @Test
    public void namesFluxImmutability(){
        Flux<String> mappedStringFlux = fluxAndMonoGeneratorService.namesFlux_Immutability();

        StepVerifier.create(mappedStringFlux)
                .expectNext("olavo", "pietro", "leo")
                .verifyComplete();
    }

    @Test
    public void namesFluxFiltering(){
        Flux<String> filteredStringFlux = fluxAndMonoGeneratorService.namesFlux_Filtering(3);

        StepVerifier.create(filteredStringFlux)
                .expectNext("5-OLAVO", "6-PIETRO")
                .verifyComplete();
    }

    @Test
    public void namesFluxFlatmap(){
        Flux<String> flatmapStringFlux = fluxAndMonoGeneratorService.namesFlux_Flatmap(3);

        StepVerifier.create(flatmapStringFlux)
                .expectNext("O", "L", "A", "V", "O", "P", "I", "E", "T", "R", "O")
                .verifyComplete();
    }

    @Test
    public void nameMonoFlatmap(){
        Mono<List<String>> listMono = fluxAndMonoGeneratorService.nameMono_Flatmap();

        StepVerifier.create(listMono)
                .expectNext(List.of("O", "L", "A", "V", "O"))
                .verifyComplete();
    }

    @Test
    public void nameMonoFlatMapMany(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.nameMono_FlatmapMany();

        StepVerifier.create(stringFlux)
                .expectNext("O", "L", "A", "V", "O")
                .verifyComplete();
    }

    @Test
    public void namesFluxTransform(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_transform(5);

        StepVerifier.create(stringFlux)
            .expectNext("P", "I", "E", "T", "R", "O")
                .verifyComplete();
    }

    @Test
    public void namesFluxTransform_WhenEmpty_defaultIfEmpty(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_transform(6);

        StepVerifier.create(stringFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void namesFluxTransform_WhenEmpty_switchIfEmpty(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_transform_EmptyData(6);

        StepVerifier.create(stringFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    public void concatTest(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.flux_Concat();

        StepVerifier.create(stringFlux)
                .expectNext("a", "b", "c", "d", "e", "f")
                .verifyComplete();
    }

    @Test
    public void concatWithTest(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.flux_ConcatWith();

        StepVerifier.create(stringFlux)
                .expectNext("a", "b")
                .verifyComplete();
    }
}
