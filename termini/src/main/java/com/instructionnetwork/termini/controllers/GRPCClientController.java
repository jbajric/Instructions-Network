package com.instructionnetwork.termini.controllers;

import com.instructionnetwork.termini.services.GRPCClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GRPCClientController {
    GRPCClientService   grpcClientService;

    @Autowired
    public GRPCClientController(GRPCClientService grpcClientService) {
        this.grpcClientService = grpcClientService;
    }

}