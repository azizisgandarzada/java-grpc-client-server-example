package com.grpc.card;

import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.Map;

public class CardService extends CardServiceGrpc.CardServiceImplBase {

    @Override
    public void getCardsByUser(CardRequest request, StreamObserver<CardResponse> responseObserver) {
        System.out.println("CardRequest ==> " + request);
        responseObserver.onNext(CardResponse.newBuilder()
                .addAllCards(List.of(
                        Card.newBuilder()
                                .setHolderName("AZIZ ISGANDARZADA")
                                .setBalance(100.5D)
                                .setExpiredAt("11/22")
                                .setNumber("4567********1235")
                                .setStatus(Status.ACTIVE)
                                .setType(Type.VISA)
                                .addAllCurrencies(List.of(Currency.AZN, Currency.USD))
                                .putAllSpecifications(Map.of("Width", "54mm",
                                        "Length", "85.5mm"))
                                .build(),
                        Card.newBuilder()
                                .setHolderName("AZIZ ISGANDARZADA")
                                .setBalance(0D)
                                .setExpiredAt("11/20")
                                .setNumber("6312********4532")
                                .setStatus(Status.EXPIRED)
                                .setType(Type.MAESTRO)
                                .addAllCurrencies(List.of(Currency.RUB, Currency.USD, Currency.GBP))
                                .putAllSpecifications(Map.of("Width", "54mm",
                                        "Length", "85.5mm"))
                                .build()
                ))
                .build());
        responseObserver.onCompleted();
    }

}
