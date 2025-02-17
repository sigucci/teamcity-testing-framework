package com.example.teamcity.enums;

import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.models.BuildType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Endpoint {
    BUILD_TYPES("/app/rest/BuildTypes",BuildType.class);

    private final String url;
    private final Class<? extends BaseModel> modelClass;
}
