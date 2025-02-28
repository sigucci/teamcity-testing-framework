package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.enums.Endpoint;
import io.restassured.specification.RequestSpecification;

import java.util.EnumMap;

public class ChekedRequests {
    private final EnumMap<Endpoint, CheckedBase> requests = new EnumMap<>(Endpoint.class);

    public ChekedRequests(RequestSpecification spec) {
        for (var endpoint : Endpoint.values()) {
            requests.put(endpoint, new CheckedBase(spec, endpoint));
        }
    }

    public <T extends BaseModel> CheckedBase<T> getRequest(Endpoint endpoint) {
        return (CheckedBase<T>) requests.get(endpoint);
    }

    public RequestSpecification getSpec() {
        // Здесь возвращаем ту же спецификацию, которая была передана при создании ChekedRequests
        return requests.values().iterator().next().spec;
    }
}
