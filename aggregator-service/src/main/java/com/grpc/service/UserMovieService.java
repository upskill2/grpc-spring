package com.grpc.service;

import com.grpc.common.Genre;
import com.grpc.dto.RecommendedMovie;
import com.grpc.dto.UserGenreUpdateResponse;
import com.grpc.movie.MovieSearchRequest;
import com.grpc.movie.MovieSearchResponse;
import com.grpc.movie.MovieServiceGrpc;
import com.grpc.user.UserGenreUpdateRequest;
import com.grpc.user.UserSearchRequest;
import com.grpc.user.UserSearchResponse;
import com.grpc.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMovieService {
    @GrpcClient ("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    @GrpcClient ("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    public List<RecommendedMovie> getRecommendedMovies (String loginId) {
        UserSearchRequest userSearchRequest = UserSearchRequest
                .newBuilder ()
                .setLoginId (loginId)
                .build ();

        final UserSearchResponse userSearchResponse = userStub.searchUserGenre (userSearchRequest);

        final MovieSearchRequest movieRequest = MovieSearchRequest.newBuilder ()
                .setGenre (userSearchResponse.getGenre ())
                .build ();

        final MovieSearchResponse movieSearchResponse = movieStub.searchMovies (movieRequest);

        return movieSearchResponse.getMoviesList ()
                .stream ()
                .map (movie -> RecommendedMovie.builder ()
                        .title (movie.getTitle ())
                        .year (movie.getYear ())
                        .rating (movie.getRating ())
                        .genre (movie.getGenre ().name ())
                        .build ())
                .toList ();
    }

    public UserGenreUpdateResponse setUserGenre (String loginId, String genre) {
        final UserSearchResponse userSearchResponse = userStub.updateUserGenre (UserGenreUpdateRequest.newBuilder ()
                .setLoginId (loginId)
                .setGenre (Genre.valueOf (genre.toUpperCase ()))
                .build ());

        return UserGenreUpdateResponse.builder ()
                .loginId (userSearchResponse.getLoginId ())
                .name (userSearchResponse.getName ())
                .genre (userSearchResponse.getGenre ().name ())
                .build ();
    }

}
