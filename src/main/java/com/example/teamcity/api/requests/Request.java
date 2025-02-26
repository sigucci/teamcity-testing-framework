package com.example.teamcity.api.requests;

import com.example.teamcity.enums.Endpoint;
import io.restassured.specification.RequestSpecification;

public class Request {
    public final RequestSpecification spec;
    protected final Endpoint endpoint;

    public Request(RequestSpecification spec, Endpoint endpoint) {
        this.spec = spec;
        this.endpoint = endpoint;
    }
}
