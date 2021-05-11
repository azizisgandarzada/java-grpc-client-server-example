package com.grpc.user;

import com.grpc.card.AddCardResponse;
import com.grpc.card.Card;
import com.grpc.card.DeleteCardResponse;
import com.grpc.card.GetCardResponse;
import com.grpc.card.UpdateCardResponse;

public class GrpcUserClient {

    public static void main(String[] args) {
        UserService userService = new UserService();
        int userId = 1;
        GetCardResponse getCardResponse = userService.getUserCards(userId);
        System.out.println("GetCardResponse ==> " + getCardResponse);


        Card card = getCardResponse.getCards(0);
        AddCardResponse addCardResponse = userService.addUserCard(userId, card);
        System.out.println("AddCardResponse ==> " + addCardResponse);


        Card updatedCard = Card.newBuilder()
                .mergeFrom(card)
                .setId(10)
                .build();
        UpdateCardResponse updateCardResponse = userService.updateUserCard(userId, updatedCard);
        System.out.println("UpdateCardResponse ==> " + updateCardResponse);

        DeleteCardResponse deleteCardResponse = userService.deleteUserCard(userId, card.getId());
        System.out.println("DeleteCardResponse ==> " + deleteCardResponse);
    }

}
