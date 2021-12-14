package com.learnreactiveprogramming.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
        //db or remote service call
        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .log();
    }

    public Mono<String> nameMono(){
        return Mono.just("Olavo");
    }

    public Flux<String> namesFlux_Map(){
        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .map(String::toUpperCase);
    }

    public Flux<String> namesFlux_Immutability(){
        Flux<String> stringFlux = Flux.fromIterable(List.of("olavo", "pietro", "leo"));
        stringFlux.map(String::toUpperCase);
        return stringFlux;
    }

    public Flux<String> namesFlux_Filtering(int stringLength){
        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .map(String::toUpperCase)
                .filter(o -> o.length() > stringLength)
                .map(s -> s.length() + "-" + s);
    }

    public Flux<String> namesFlux_Flatmap(int stringLength){
        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .map(String::toUpperCase)
                .filter(o -> o.length() > stringLength)
                .flatMap(s -> splitString(s));

    }

    public Mono<List<String>> nameMono_Flatmap(){
        return Mono.just("olavo")
                .map(String::toUpperCase)
                .flatMap(this::splitStringMono);
    }

    public Flux<String> nameMono_FlatmapMany(){
        return Mono.just("olavo")
                .map(String::toUpperCase)
                .flatMapMany(this::splitString);
    }

    public Flux<String> namesFlux_transform(int stringLength){
        Function<Flux<String>, Flux<String>> mapFilter = name -> name.map(String::toUpperCase)
                .filter(o -> o.length() > stringLength);

        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .transform(mapFilter)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_EmptyData(int stringLength){
        Function<Flux<String>, Flux<String>> mapFilter = name -> name.map(String::toUpperCase)
                .filter(o -> o.length() > stringLength);

        Flux<String> defaultPublisher = Flux.just("default")
                .transform(mapFilter);

        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .transform(mapFilter)
                .switchIfEmpty(defaultPublisher)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> flux_Concat(){
        Flux<String> abcFlux = Flux.just("a", "b", "c");
        Flux<String> defFlux = Flux.just("d", "e", "f");

        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> flux_ConcatWith(){
        Mono<String> aMono = Mono.just("a");
        Mono<String> bMono = Mono.just("b");

        return aMono.concatWith(bMono);
    }





    private Flux<String> splitString(String s) {
        String[] split = s.split("");
        return Flux.fromArray(split);
    }

    private Mono<List<String>> splitStringMono(String s) {
        String[] split = s.split("");
        List<String> split1 = List.of(split);
        return Mono.just(split1);
    }


    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(s -> {
                    System.out.println("names: " + s);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(s -> {
                    System.out.println("Mono name: " + s);
                });
    }

}
