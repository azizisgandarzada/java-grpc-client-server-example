package com.grpc.user;

import com.grpc.card.CardRequest;
import com.grpc.card.CardResponse;
import com.grpc.card.CardServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class UserService {

    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8085)
                .usePlaintext()
                .build();
        CardServiceGrpc.CardServiceBlockingStub stub = CardServiceGrpc.newBlockingStub(managedChannel);
        CardResponse cardResponse = stub.getCardsByUser(CardRequest.newBuilder()
                .setUserId(1)
                .build());
        System.out.println("CardResponse ==> " + cardResponse);
        managedChannel.shutdown();
    }

}
