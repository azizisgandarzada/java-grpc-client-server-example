package com.grpc.card.interceptor;

import com.grpc.card.ErrorCode;
import com.grpc.card.ErrorResponse;
import com.grpc.card.exception.CardNotFoundException;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler implements ServerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {

            // this method is triggered to call our overridden methods
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception ex) {
                    handleException(ex, call, headers);
                }
            }

            // this method is triggered when our overridden methods is completed
            @Override
            public void onComplete() {
                super.onComplete();
            }

            // this method is triggered before calling our overridden methods
            @Override
            public void onReady() {
                super.onReady();
            }

            private void handleException(Exception ex, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
                LOG.error("Occurred exception", ex);
                if (ex instanceof CardNotFoundException) {
                    ErrorResponse errorResponse = ErrorResponse.newBuilder()
                            .setErrorCode(ErrorCode.CARD_NOT_FOUND)
                            .setErrorMessage("Card Not Found!")
                            .setTimestamp(Instant.now().toEpochMilli())
                            .build();
                    metadata = getErrorMetadata(errorResponse);
                    serverCall.close(Status.NOT_FOUND.withDescription(ex.getMessage()), metadata);
                } else {
                    serverCall.close(Status.UNKNOWN, metadata);
                }
            }
        };
    }

    private Metadata getErrorMetadata(ErrorResponse errorResponse) {
        Metadata metadata = new Metadata();
        Metadata.Key<ErrorResponse> responseKey = ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance());
        metadata.put(responseKey, errorResponse);
        return metadata;
    }


}
