package com.grpc.service;

import com.grpc.common.Genre;
import com.grpc.movie.MovieDto;
import com.grpc.movie.MovieSearchRequest;
import com.grpc.movie.MovieSearchResponse;
import com.grpc.movie.MovieServiceGrpc;
import com.grpc.repository.MovieRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void searchMovies (final MovieSearchRequest request, final StreamObserver<MovieSearchResponse> responseObserver) {
        final List<MovieDto> list = movieRepository.findByGenreOrderByYearDesc (request.getGenre ().name ().toUpperCase ())
                .stream ().map (movies -> MovieDto.newBuilder ()
                        .setTitle (movies.getTitle ())
                        .setYear (movies.getYear ())
                        .setRating (movies.getRating ())
                        .setGenre (Genre.valueOf (movies.getGenre ().toUpperCase ()))
                        .build ()).toList ();

        responseObserver.onNext (MovieSearchResponse.newBuilder ()
                .addAllMovies (list)
                .build ());
        responseObserver.onCompleted ();
    }
}
