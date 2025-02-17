package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hamcrest.Condition;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Steps {
    private Integer count;
    private List<Step> step;
}
