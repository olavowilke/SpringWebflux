package com.learnreactiveprogramming.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;


public class FluxAndMonoExamples {

    /*
    --------------------------------------------------------------------------------------------------------------------
    UDEMY exercises
    --------------------------------------------------------------------------------------------------------------------
     */

    public Flux<String> namesFlux() {
        //db or remote service call
        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .log();
    }

    public Mono<String> nameMono() {
        return Mono.just("Olavo");
    }

    public Flux<String> namesFlux_Map() {
        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .map(String::toUpperCase);
    }

    public Flux<String> namesFlux_Immutability() {
        Flux<String> stringFlux = Flux.fromIterable(List.of("olavo", "pietro", "leo"));
        stringFlux.map(String::toUpperCase);
        return stringFlux;
    }

    public Flux<String> namesFlux_Filtering(int stringLength) {
        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .map(String::toUpperCase)
                .filter(o -> o.length() > stringLength)
                .map(s -> s.length() + "-" + s);
    }

    public Flux<String> namesFlux_Flatmap(int stringLength) {
        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .map(String::toUpperCase)
                .filter(o -> o.length() > stringLength)
                .flatMap(s -> splitString(s));

    }

    public Mono<List<String>> nameMono_Flatmap() {
        return Mono.just("olavo")
                .map(String::toUpperCase)
                .flatMap(this::splitStringMono);
    }

    public Flux<String> nameMono_FlatmapMany() {
        return Mono.just("olavo")
                .map(String::toUpperCase)
                .flatMapMany(this::splitString);
    }

    public Flux<String> namesFlux_transform(int stringLength) {
        Function<Flux<String>, Flux<String>> mapFilter = name -> name.map(String::toUpperCase)
                .filter(o -> o.length() > stringLength);

        return Flux.fromIterable(List.of("olavo", "pietro", "leo"))
                .transform(mapFilter)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_EmptyData(int stringLength) {
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

    public Flux<String> flux_Concat() {
        Flux<String> abcFlux = Flux.just("a", "b", "c");
        Flux<String> defFlux = Flux.just("d", "e", "f");

        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> flux_ConcatWith() {
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

    /*
    --------------------------------------------------------------------------------------------------------------------
    Tech.io exercises
    --------------------------------------------------------------------------------------------------------------------
     */

    //Flux

    // TODO Return an empty Flux
    private Flux<String> emptyFlux() {
        return Flux.empty();
    }

    // TODO Return a Flux that contains 2 values "foo" and "bar" without using an array or a collection
    private Flux<String> fooBarFluxFromValues() {
        return Flux.just("foo", "bar");
    }

    // TODO Create a Flux from a List that contains 2 values "foo" and "bar"
    private Flux<String> fooBarFluxFromList() {
        return Flux.fromIterable(List.of("foo", "bar"));
    }

    // TODO Create a Flux that emits an IllegalStateException
    private Flux<String> errorFlux() {
        return Flux.error(new IllegalStateException());
    }

    // TODO Create a Flux that emits increasing values from 0 to 9 each 100ms
    private Flux<Long> counter() {
        return Flux.interval(Duration.ofMillis(100)).take(10);
    }

    //Mono

    // TODO Return an empty Mono
    private Mono<String> emptyMono() {
        return Mono.empty();
    }

    // TODO Return a Mono that never emits any signal
    private Mono<String> monoWithNoSignal() {
        return Mono.never();
    }

    // TODO Return a Mono that contains a "foo" value
    private Mono<String> fooMono() {
        return Mono.just("foo");
    }


    // TODO Create a Mono that emits an IllegalStateException
    private Mono<String> errorMono() {
        return Mono.error(new IllegalStateException());
    }

    //Transforming

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class User {
        private String username;
        private String firstName;
        private String lastName;
    }

    // TODO Capitalize the user username, firstname and lastname
    private Mono<User> capitalizeMono(Mono<User> user$) {
        return user$.map(user ->
                        new User(
                                user.getUsername().toUpperCase(),
                                user.getFirstName().toUpperCase(),
                                user.getLastName().toUpperCase()))
                .log();
    }

    // TODO Capitalize the users username, firstName and lastName
    Flux<User> capitalizeFlux(Flux<User> user$$) {
        return user$$.map(user ->
                        new User(
                                user.getUsername().toUpperCase(),
                                user.getFirstName().toUpperCase(),
                                user.getLastName().toUpperCase()))
                .log();
    }

    // TODO Capitalize the users username, firstName and lastName using #asyncCapitalizeUser
    Flux<User> asyncCapitalizeFlux(Flux<User> user$$) {
        return user$$.flatMap(user ->
                        Mono.just(
                                new User(
                                        user.getUsername().toUpperCase(),
                                        user.getFirstName().toUpperCase(),
                                        user.getLastName().toUpperCase()
                                )
                        )
                )
                .log();
    }

    Mono<User> asyncCapitalizeUser(User u) {
        return Mono.just(
                new User(
                        u.getUsername().toUpperCase(),
                        u.getFirstName().toUpperCase(),
                        u.getLastName().toUpperCase()
                )
        );
    }

    //Merging

    /*TODO Merge flux1 and flux2 values with interleave
    The caveat here is that values from flux1 arrive with a delay,
    so in the resulting Flux we start seeing values from flux2 first.*/
    Flux<User> mergeFluxWithInterleave(Flux<User> user1$$, Flux<User> user2$$) {
        return user1$$.mergeWith(user2$$)
                .log();
    }

    // TODO Merge flux1 and flux2 values with no interleave (flux1 values and then flux2 values)
    //concat will preserve the order
    Flux<User> mergeFluxWithNoInterleave(Flux<User> user1$$, Flux<User> user2$$) {
        return user1$$.concatWith(user2$$);
    }

    // TODO Create a Flux containing the value of mono1 then the value of mono2
    Flux<User> createFluxFromMultipleMono(Mono<User> user1$, Mono<User> user2$) {
        return user1$.concatWith(user2$);
    }

    //Error

    // TODO Return a Mono<User> containing User.WALTER when an error occurs in the input Mono, else
    //  do not change the input Mono.
    private Mono<User> onErrorReturnMono(Mono<User> user$) {
        return user$
                .onErrorReturn(new User("olavowilke", "olavo", "wilke"));
    }

    // TODO Return a Flux<User> containing User.SAUL and User.JESSE when an error occurs in the input Flux, else
    //  do not change the input Flux.
    Flux<User> onErrorResumeWithFlux(Flux<User> user1$$) {
        User user1 = new User("jessepinkman", "jesse", "pinkman");
        User user2 = new User("walterwhite", "walter", "white");
        Flux<User> user2$$ = Flux.just(user1, user2);

        return user1$$
                .onErrorResume(throwable -> user2$$);
    }


    public static void main(String[] args) {

        FluxAndMonoExamples fluxAndMonoExamples = new FluxAndMonoExamples();
        User user1 = new User("olavowilke", "olavo", "wilke");
        User user2 = new User("narutouzumaki", "naruto", "uzumaki");
//----------------------------------------------------------------------------------------------------------------------
//        fluxAndMonoExamples.namesFlux()
//                .subscribe(s -> {
//                    System.out.println("names: " + s);
//                });
//
//        fluxAndMonoExamples.nameMono()
//                .subscribe(s -> {
//                    System.out.println("Mono name: " + s);
//                });
//
//        fluxAndMonoExamples.fooBarFluxFromList()
//                .subscribe();

//        fluxAndMonoExamples.counter()
//                .subscribe();
//----------------------------------------------------------------------------------------------------------------------
//        Mono<User> user$ = Mono.just(user1);
//        fluxAndMonoExamples.capitalizeMono(user$)
//                .subscribe(System.out::println);

//        Flux<User> user$$ = Flux.fromIterable(List.of(user1, user2));
//        fluxAndMonoExamples.capitalizeFlux(user$$)
//                .subscribe(System.out::println);

//        fluxAndMonoExamples.asyncCapitalizeFlux(user$$)
//                .subscribe();
//----------------------------------------------------------------------------------------------------------------------
        Flux<User> user1$$ = Flux.just(user1);
        Flux<User> user2$$ = Flux.just(user2);
        fluxAndMonoExamples.mergeFluxWithInterleave(user1$$, user2$$)
                .subscribe(System.out::println);
    }

}
