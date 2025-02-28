package com.example.teamcity.projectTest;

import com.example.teamcity.api.BaseApiTest;
import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.models.ParentProject;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.SourceProject;
import com.example.teamcity.api.requests.checked.ChekedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.enums.Endpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class ProjectNegativeTest extends BaseApiTest {

    @Test(description = "Создание проекта без Name")
    public void userCanNotCreateProjectWithoutNameTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(false);
        project.setName(null);

        UncheckedBase unchecked = new UncheckedBase(Specifications.authSpec(testData.getUser()), Endpoint.PROJECTS);
        Response response = unchecked.create(project);

        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status code was not 400 Bad Request");
    }

    @Test(description = "Создание проекта с пустым телом запроса")
    public void userCanNotCreateProjectWithEmptyBodyTest() {
        ChekedRequests userCheckRequests = createUser();
        RequestSpecification spec = userCheckRequests.getSpec();
        Endpoint projectsEndpoint = Endpoint.PROJECTS;

        Response response = RestAssured
                .given()
                .spec(spec)
                .body("null")
                .post(projectsEndpoint.getUrl());

        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_INTERNAL_SERVER_ERROR, "Status code was not 500 Internal Server Error");
    }

    @Test(description = "Создание проекта с некорректно сформированным JSON (после модификации валидного объекта)")
    public void userCanNotCreateProjectWithInvalidJsonTest() throws JsonProcessingException {
        ChekedRequests userCheckRequests = createUser();
        Endpoint projectsEndpoint = Endpoint.PROJECTS;
        Project project = TestDataGenerator.generateProject(false);

        ObjectMapper mapper = new ObjectMapper();
        String validJson = mapper.writeValueAsString(project);
        String invalidJson = validJson.substring(0, validJson.length() - 1);

        RequestSpecification spec = userCheckRequests.getSpec();
        Response response = io.restassured.RestAssured
                .given()
                .spec(spec)
                .body(invalidJson)
                .post(projectsEndpoint.getUrl());

        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status code was not 400 Bad Request");
    }

    @Test(description = "Создание проекта с дублированным id")
    public void userCanNotCreateProjectWithDuplicatedIdTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(false);
        Project createdProject = createProject(userCheckRequests, project);

        Project duplicatedIdProject = TestDataGenerator.generateProject(false);
        duplicatedIdProject.setId(createdProject.getId());

        UncheckedBase unchecked = new UncheckedBase(userCheckRequests.getSpec(), Endpoint.PROJECTS);
        Response response = unchecked.create(duplicatedIdProject);

        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status code was not 400 Bad Request");
    }

    @Test(description = "Создание проекта с несуществующим parentProject проекта")
    public void userCanNotCreateProjectWithNotExistingParentProjectTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(true);
        project.setParentProject( new ParentProject("test_not_existing_parentProject"));

        UncheckedBase unchecked = new UncheckedBase(userCheckRequests.getSpec(), Endpoint.PROJECTS);
        Response response = unchecked.create(project);

        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Status code was not 404 Not Found");
    }

    @Test(description = "Создание проекта с несуществующим sourceProject проекта")
    public void userCanNtCreateProjectWithNotExistingSourceProjectTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(true);
        project.setSourceProject( new SourceProject("test_not_existing_sourceProject"));

        UncheckedBase unchecked = new UncheckedBase(userCheckRequests.getSpec(), Endpoint.PROJECTS);
        Response response = unchecked.create(project);

        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Status code was not 404 Not Found");
    }
}