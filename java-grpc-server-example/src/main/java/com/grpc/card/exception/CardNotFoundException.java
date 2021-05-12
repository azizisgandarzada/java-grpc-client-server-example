package com.grpc.card.exception;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException() {
        super("Card Not Found!");
    }

}
