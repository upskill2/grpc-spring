package com.grpc.service;

import com.grpc.common.Genre;
import com.grpc.repository.UserRepository;
import com.grpc.user.UserGenreUpdateRequest;
import com.grpc.user.UserSearchRequest;
import com.grpc.user.UserSearchResponse;
import com.grpc.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void searchUserGenre (final UserSearchRequest request, final StreamObserver<UserSearchResponse> responseObserver) {
        userRepository.findById (request.getLoginId ()).ifPresent (user -> {
            final UserSearchResponse response = UserSearchResponse.newBuilder ()
                    .setLoginId (user.getLogin ())
                    .setName (user.getName ())
                    .setGenre (Genre.valueOf (user.getGenre ().toUpperCase ()))
                    .build ();
            responseObserver.onNext (response);
            responseObserver.onCompleted ();
        });
    }

    @Override
    @Transactional
    public void updateUserGenre (final UserGenreUpdateRequest request, final StreamObserver<UserSearchResponse> responseObserver) {
        userRepository.findById (request.getLoginId ()).ifPresent (user -> {
            user.setGenre (String.valueOf (Genre.valueOf (request.getGenre ().name ().toUpperCase ())));
            userRepository.save (user);
            final UserSearchResponse response = UserSearchResponse.newBuilder ()
                    .setLoginId (user.getLogin ())
                    .setName (user.getName ())
                    .setGenre (Genre.valueOf (user.getGenre ()))
                    .build ();
            responseObserver.onNext (response);
            responseObserver.onCompleted ();
        });
    }
}
