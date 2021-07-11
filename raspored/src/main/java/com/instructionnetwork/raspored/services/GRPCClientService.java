package com.instructionnetwork.raspored.services;

import com.instructionnetwork.systemevents.ActionsRequest;
import com.instructionnetwork.systemevents.ActionsResponse;
import com.instructionnetwork.systemevents.SystemEventsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class GRPCClientService {


    public String save(String microservice, String actionType, String resourceName, String responseType) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("systemevents", 9090)
                .usePlaintext()
                .build();
        SystemEventsServiceGrpc.SystemEventsServiceBlockingStub stub = SystemEventsServiceGrpc.newBlockingStub(channel);
        ActionsResponse helloResponse = stub.save(ActionsRequest.newBuilder()
                .setTimestamp(new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss").format(new Date()))
                .setMicroservice(microservice)
                .setActionType(actionType)
                .setResourceName(resourceName)
                .setResponseType(responseType)
                .build());
        channel.shutdown();
        return helloResponse.getStatus();
    }

}