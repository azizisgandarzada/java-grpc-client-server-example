package com.grpc.card;

import com.grpc.card.interceptor.ExceptionHandler;
import com.grpc.card.interceptor.LoggingInterceptor;
import com.grpc.card.service.CardService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcCardServer {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCardServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8085)
                .addService(new CardService())
                .intercept(new ExceptionHandler())
                .intercept(new LoggingInterceptor())
                .build();
        server.start();
        LOG.info("gRPC server Started");
        server.awaitTermination();
    }

}
