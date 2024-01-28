package com.grpc.controller;

import com.grpc.dto.RecommendedMovie;
import com.grpc.dto.UserGenre;
import com.grpc.dto.UserGenreUpdateResponse;
import com.grpc.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/aggregator/user")
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;

    @GetMapping ("/{loginId}")
    public List<RecommendedMovie> getRecommendedMovies (@PathVariable String loginId) {
        return userMovieService.getRecommendedMovies (loginId);
    }


    @PutMapping ()
    public ResponseEntity setUserGenre (@RequestBody UserGenre userGenre) {
        final UserGenreUpdateResponse userGenreUpdateResponse = userMovieService.setUserGenre (userGenre.getLoginId (), userGenre.getGenre ());
        return ResponseEntity.ok (userGenreUpdateResponse);
    }
}
