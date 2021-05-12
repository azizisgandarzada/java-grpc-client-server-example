package com.grpc.user.service;

import com.grpc.card.AddCardRequest;
import com.grpc.card.AddCardResponse;
import com.grpc.card.Card;
import com.grpc.card.CardServiceGrpc;
import com.grpc.card.DeleteCardRequest;
import com.grpc.card.DeleteCardResponse;
import com.grpc.card.ErrorResponse;
import com.grpc.card.GetCardRequest;
import com.grpc.card.GetCardResponse;
import com.grpc.card.UpdateCardRequest;
import com.grpc.card.UpdateCardResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public GetCardResponse getUserCards(int userId) {
        ManagedChannel managedChannel = openChannel();
        CardServiceGrpc.CardServiceBlockingStub stub = CardServiceGrpc.newBlockingStub(managedChannel);
        try {
            return stub.getCards(GetCardRequest.newBuilder()
                    .setUserId(userId)
                    .build());
        } catch (Exception ex) {
            log(ex);
            return null;
        } finally {
            managedChannel.shutdown();
        }
    }

    public AddCardResponse addUserCard(int userId, Card card) {
        ManagedChannel managedChannel = openChannel();
        CardServiceGrpc.CardServiceBlockingStub stub = CardServiceGrpc.newBlockingStub(managedChannel);
        try {
            return stub.addCard(AddCardRequest.newBuilder()
                    .setUserId(userId)
                    .setCard(card)
                    .build());
        } catch (Exception ex) {
            log(ex);
            return null;
        } finally {
            managedChannel.shutdown();
        }
    }

    public UpdateCardResponse updateUserCard(int userId, Card card) {
        ManagedChannel managedChannel = openChannel();
        CardServiceGrpc.CardServiceBlockingStub stub = CardServiceGrpc.newBlockingStub(managedChannel);
        try {
            return stub.updateCard(UpdateCardRequest.newBuilder()
                    .setUserId(userId)
                    .setCard(card)
                    .build());
        } finally {
            managedChannel.shutdown();
        }
    }

    public DeleteCardResponse deleteUserCard(int userId, int cardId) {
        ManagedChannel managedChannel = openChannel();
        CardServiceGrpc.CardServiceBlockingStub stub = CardServiceGrpc.newBlockingStub(managedChannel);
        try {
            return stub.deleteCard(DeleteCardRequest.newBuilder()
                    .setUserId(userId)
                    .setCardId(cardId)
                    .build());
        } catch (Exception ex) {
            log(ex);
            return null;
        } finally {
            managedChannel.shutdown();
        }
    }

    private ManagedChannel openChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 8085)
                .usePlaintext()
                .build();
    }

    private void log(Exception ex) {
        Status status = Status.fromThrowable(ex);
        Metadata metadata = Status.trailersFromThrowable(ex);
        ErrorResponse errorResponse = metadata.get(ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance()));
        LOG.error("Status {}, Headers {}, ErrorResponse {}", status, metadata, errorResponse);
    }

}
