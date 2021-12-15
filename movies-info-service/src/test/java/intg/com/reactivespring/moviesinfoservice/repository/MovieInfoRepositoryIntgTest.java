package com.reactivespring.moviesinfoservice.repository;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
@ActiveProfiles: if not specified, the profile will be default, and Spring will try to run
the configs in the application.yml
@DataMongoTest: for testing the database layer
From Spring 2.6.x, the embedded mongodb version must be specified, as below.
For earlier versions, this annotation is not necessary
 */
@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
public class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {

        List<MovieInfo> movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieInfos)
                .blockLast();
        /*
        blockLast() will assure that saveAll will get completed before calling the method on the test case,
        as the calls are asynchronous
         */
    }

    @AfterEach
    void clearDatabase() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAllTest() {
        Flux<MovieInfo> all = movieInfoRepository.findAll().log();

        StepVerifier.create(all)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findByIdTest() {
        Mono<MovieInfo> abc = movieInfoRepository.findById("abc").log();

        StepVerifier.create(abc)
                .assertNext(mono -> {
                    assertEquals("Dark Knight Rises", mono.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        //given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Finishes",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        //when
        Mono<MovieInfo> abc = movieInfoRepository.save(movieInfo).log();

        //then
        StepVerifier.create(abc)
                .assertNext(mono -> {
                    assertNotNull(movieInfo.getMovieInfoId());
                    assertEquals("Batman Finishes", mono.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        MovieInfo movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2021);

        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(movieInfo).log();

        StepVerifier.create(movieInfoMono)
                .assertNext(mono -> {
                    assertEquals(2021, mono.getYear());
                })
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        movieInfoRepository.deleteById("abc").block();
        Flux<MovieInfo> moviesInfoFlux = movieInfoRepository.findAll().log();

        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

}
