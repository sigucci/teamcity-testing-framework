package com.example.teamcity.api.generator;

import com.example.teamcity.api.models.Project;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Consumer;

public final class NegativeTestDataGenerator {

    private NegativeTestDataGenerator() {
    }

    public static String generateModifiedProjectJson(Consumer<Project> modifier) {
        Project project = TestDataGenerator.generateProject(true);
        modifier.accept(project);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(project);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации объекта Project", e);
        }
    }
}