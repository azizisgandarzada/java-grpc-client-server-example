package com.grpc.card;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class GrpcCardServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8085)
                .addService(new CardService())
                .build();
        server.start();
        server.awaitTermination();
    }

}
