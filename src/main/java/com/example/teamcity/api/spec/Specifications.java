package com.example.teamcity.api.spec;

import java.util.List;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class Specifications {
    private static Specifications spec;

    private Specifications() {
    }

    public static Specifications getSpec() {
        if (spec == null) {
            spec = new Specifications();
        }
        return spec;
    }

    private RequestSpecBuilder requestBuilder() {
        RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        requestBuilder.setBaseUri("http://" + Config.getProperty("host")).build();
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        requestBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        return requestBuilder;
    }

    public RequestSpecification unauthSpec() {
        return requestBuilder().build();
    }

    public RequestSpecification authSpec(User user) {
        BasicAuthScheme basicAuthScheme = new BasicAuthScheme();
        basicAuthScheme.setUserName(user.getUser());
        basicAuthScheme.setPassword(user.getPassword());
        return requestBuilder()
                .setAuth(basicAuthScheme)
                .build();
    }

}
