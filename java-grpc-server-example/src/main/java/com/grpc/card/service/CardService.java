package com.grpc.card.service;

import com.grpc.card.AddCardRequest;
import com.grpc.card.AddCardResponse;
import com.grpc.card.Card;
import com.grpc.card.CardServiceGrpc;
import com.grpc.card.Currency;
import com.grpc.card.DeleteCardRequest;
import com.grpc.card.DeleteCardResponse;
import com.grpc.card.ErrorCode;
import com.grpc.card.ErrorResponse;
import com.grpc.card.GetCardRequest;
import com.grpc.card.GetCardResponse;
import com.grpc.card.Status;
import com.grpc.card.Type;
import com.grpc.card.UpdateCardRequest;
import com.grpc.card.UpdateCardResponse;
import com.grpc.card.exception.CardNotFoundException;
import io.grpc.Metadata;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class CardService extends CardServiceGrpc.CardServiceImplBase {

    private static final List<Card> CARDS = new ArrayList<>();

    static {
        CARDS.add(Card.newBuilder()
                .setId(1)
                .setHolderName("AZIZ ISGANDARZADA")
                .setBalance(100.5D)
                .setExpiredAt("11/22")
                .setNumber("4567673421481235")
                .setStatus(Status.ACTIVE)
                .setType(Type.VISA)
                .addAllCurrencies(List.of(Currency.AZN, Currency.USD))
                .putAllSpecifications(Map.of("Width", "54mm",
                        "Length", "85.5mm"))
                .build());
        CARDS.add(Card.newBuilder()
                .setId(2)
                .setHolderName("AZIZ ISGANDARZADA")
                .setBalance(0D)
                .setExpiredAt("11/20")
                .setNumber("6312857327154532")
                .setStatus(Status.EXPIRED)
                .setType(Type.MAESTRO)
                .addAllCurrencies(List.of(Currency.RUB, Currency.USD, Currency.GBP))
                .putAllSpecifications(Map.of("Width", "54mm",
                        "Length", "85.5mm"))
                .build());
        CARDS.add(Card.newBuilder()
                .setId(3)
                .setHolderName("AZIZ ISGANDARZADA")
                .setBalance(10000D)
                .setExpiredAt("05/23")
                .setNumber("5537473623072312")
                .setStatus(Status.BLOCKED)
                .setType(Type.MASTERCARD)
                .addAllCurrencies(List.of(Currency.AZN, Currency.USD, Currency.GBP))
                .putAllSpecifications(Map.of("Width", "54mm",
                        "Length", "85.5mm"))
                .build());
    }

    @Override
    public void getCards(GetCardRequest request, StreamObserver<GetCardResponse> responseObserver) {
        System.out.println("GetCardRequest ==> " + request);
        responseObserver.onNext(GetCardResponse.newBuilder()
                .addAllCards(CARDS)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void addCard(AddCardRequest request, StreamObserver<AddCardResponse> responseObserver) {
        System.out.println("AddCardRequest ==> " + request);
        Card card = CARDS.stream()
                .filter(c -> c.getNumber().equals(request.getCard().getNumber()))
                .findFirst()
                .orElse(null);
        if (card != null) {
            ErrorResponse errorResponse = ErrorResponse.newBuilder()
                    .setErrorCode(ErrorCode.CARD_ALREADY_EXISTS)
                    .setErrorMessage("Card Already Exists!")
                    .setTimestamp(Instant.now().toEpochMilli())
                    .build();
            Metadata metadata = getErrorMetadata(errorResponse);
            responseObserver.onError(io.grpc.Status.ALREADY_EXISTS
                    .withDescription("Card Already Exists!")
                    .asRuntimeException(metadata));
            responseObserver.onCompleted();
            return;
        }
        CARDS.add(request.getCard());
        responseObserver.onNext(AddCardResponse.newBuilder()
                .setCards(request.getCard())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateCard(UpdateCardRequest request, StreamObserver<UpdateCardResponse> responseObserver) {
        System.out.println("UpdateCardRequest ==> " + request);
        int index = IntStream.range(0, CARDS.size())
                .filter(i -> CARDS.get(i).getId() == request.getCard().getId())
                .findFirst()
                .orElse(-1);
        UpdateCardResponse.Builder builder = UpdateCardResponse.newBuilder();
        if (index == -1) {
            ErrorResponse errorResponse = ErrorResponse.newBuilder()
                    .setErrorCode(ErrorCode.CARD_NOT_FOUND)
                    .setErrorMessage("Card Not Found!")
                    .setTimestamp(Instant.now().toEpochMilli())
                    .build();
            builder.setErrorResponse(errorResponse);
        } else {
            CARDS.add(index, request.getCard());
            builder.setCards(request.getCard());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCard(DeleteCardRequest request, StreamObserver<DeleteCardResponse> responseObserver) {
        System.out.println("DeleteCardRequest ==> " + request);
        int index = IntStream.range(0, CARDS.size())
                .filter(i -> CARDS.get(i).getId() == request.getCardId())
                .findFirst()
                .orElse(-1);
        if (index == -1) {
            throw new CardNotFoundException();
        }
        CARDS.remove(index);
        responseObserver.onNext(DeleteCardResponse.newBuilder()
                .setDeleted(true)
                .build());
        responseObserver.onCompleted();
    }

    private Metadata getErrorMetadata(ErrorResponse errorResponse) {
        Metadata metadata = new Metadata();
        Metadata.Key<ErrorResponse> responseKey = ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance());
        metadata.put(responseKey, errorResponse);
        return metadata;
    }

}
