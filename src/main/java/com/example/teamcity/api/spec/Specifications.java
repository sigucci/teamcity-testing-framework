package com.example.teamcity.api.spec;

import java.nio.file.Paths;
import java.util.List;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.github.viclovsky.swagger.coverage.FileSystemOutputWriter;
import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static com.github.viclovsky.swagger.coverage.SwaggerCoverageConstants.OUTPUT_DIRECTORY;


public class Specifications {
    private static Specifications spec;

    private static RequestSpecBuilder reqBuilder() {
        RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        requestBuilder.addFilter(new RequestLoggingFilter());
        requestBuilder.addFilter(new ResponseLoggingFilter());
        requestBuilder.addFilter(new SwaggerCoverageRestAssured(
                new FileSystemOutputWriter(
                        Paths.get("target/" + OUTPUT_DIRECTORY)
                )
        ));
        requestBuilder.setBaseUri("http://" + Config.getProperty("host")).build();
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        requestBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        return requestBuilder;
    }

    public static RequestSpecification superUserSpec() {
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://%s:%s@%s".formatted("", Config.getProperty("superUserToken"), Config.getProperty("host")));
        return requestBuilder.build();
    }

    public static RequestSpecification unauthSpec() {
        var requestBuilder = reqBuilder();
        return requestBuilder.build();
    }

    public static RequestSpecification authSpec(User user) {
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")));
        return requestBuilder.build();
    }
}