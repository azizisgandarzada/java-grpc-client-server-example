package com.grpc.user;

import com.grpc.card.AddCardResponse;
import com.grpc.card.Card;
import com.grpc.card.DeleteCardResponse;
import com.grpc.card.GetCardResponse;
import com.grpc.card.UpdateCardResponse;
import com.grpc.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcUserClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserClient.class);

    public static void main(String[] args) {
        UserService userService = new UserService();
        int userId = 1;

        GetCardResponse getCardResponse = userService.getUserCards(userId);
        LOG.info("GetCardResponse {}", getCardResponse);


        Card card = getCardResponse.getCards(0);
        AddCardResponse addCardResponse = userService.addUserCard(userId, card);
        LOG.info("AddCardResponse {} ", addCardResponse);

        Card updatedCard = Card.newBuilder()
                .mergeFrom(card)
                .setId(10)
                .build();
        UpdateCardResponse updateCardResponse = userService.updateUserCard(userId, updatedCard);
        LOG.info("UpdateCardResponse {}", updateCardResponse);

        DeleteCardResponse deleteCardResponse = userService.deleteUserCard(userId, 10);
        LOG.info("DeleteCardResponse {} ", deleteCardResponse);
    }

}
