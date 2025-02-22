package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.enums.Endpoint;
import io.restassured.specification.RequestSpecification;

import java.util.EnumMap;

public class UnchekedRequests {
    private final EnumMap<Endpoint, UncheckedBase> requests = new EnumMap<>(Endpoint.class);

    public UnchekedRequests(RequestSpecification spec) {
        for (var endpoint : Endpoint.values()) {
            requests.put(endpoint, new UncheckedBase(spec, endpoint));
        }
    }

    public UncheckedBase getRequest(Endpoint endpoint) {
        return requests.get(endpoint);
    }
}
