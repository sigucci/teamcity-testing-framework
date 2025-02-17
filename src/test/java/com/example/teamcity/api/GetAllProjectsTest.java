package com.example.teamcity.api;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

public class GetAllProjectsTest extends BaseApiTest {
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
