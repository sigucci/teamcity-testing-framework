package com.example.teamcity.api.models;

import com.example.teamcity.api.annotations.Exclude;
import com.example.teamcity.api.annotations.Random;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project extends BaseModel {
    @Exclude
    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ParentProject parentProject = new ParentProject("_Root");
    @Random
    private String id;
    @Random
    private String name;
    @Builder.Default
    private boolean copyAllAssociatedSettings = true;

    @Exclude
    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SourceProject sourceProject = new SourceProject("_Root");

    @JsonProperty("parentProjectId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parentProjectId;
}
