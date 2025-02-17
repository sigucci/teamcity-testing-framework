package com.example.teamcity.api;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.Test;
import java.util.List;

public class DummyTest extends BaseApiTest {
    @Test
    public void userShouldBeAbleGetAllProjects() {
        RestAssured
                .given()
                .spec(Specifications.getSpec()
                        .authSpec(User.builder()
                                .user("admin").password("admin123")
                                .build()))
                .get("/app/rest/projects");
    }
}
