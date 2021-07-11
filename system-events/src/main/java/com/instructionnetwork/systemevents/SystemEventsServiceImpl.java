package com.instructionnetwork.systemevents;

import com.instructionnetwork.systemevents.model.SystemEventsModel;
import com.instructionnetwork.systemevents.repository.SystemEventsRepository;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class SystemEventsServiceImpl extends SystemEventsServiceGrpc.SystemEventsServiceImplBase {

    @Autowired
    SystemEventsRepository systemEventsRepository;

    @Override
    public void save(ActionsRequest request, StreamObserver<ActionsResponse> responseObserver) {
        SystemEventsModel event = new SystemEventsModel(
                0,
                request.getTimestamp(),
                request.getMicroservice(),
                request.getActionType(),
                request.getResourceName(),
                request.getResponseType()
        );

        systemEventsRepository.save(event);
        ActionsResponse response = ActionsResponse.newBuilder()
                .setStatus("OK")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}