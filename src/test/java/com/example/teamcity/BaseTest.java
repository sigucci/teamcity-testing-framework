package com.example.teamcity;

import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.generator.TestDataStorage;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.checked.ChekedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.enums.Endpoint;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.util.function.Consumer;

import static com.example.teamcity.enums.Endpoint.PROJECTS;
import static com.example.teamcity.enums.Endpoint.USERS;

public class BaseTest {
    protected SoftAssert softy;
    protected ChekedRequests superUserChekedRequests = new ChekedRequests(Specifications.superUserSpec());
    protected TestData testData;

    protected TestData getTestData() {
        boolean generateFullData = Boolean.parseBoolean(System.getProperty("generateFullData", "false"));
        return generateFullData ? TestDataGenerator.generateAll() : TestDataGenerator.generateWithoutExcluded();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        softy = new SoftAssert();
        testData = getTestData();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        softy.assertAll();
        TestDataStorage.getStorage().deleteCreatedEntities();
    }

    protected ChekedRequests createUser() {
        superUserChekedRequests.getRequest(USERS).create(testData.getUser());
        return new ChekedRequests(Specifications.authSpec(testData.getUser()));
    }
    protected Project createProject(ChekedRequests requests, Project project) {
        Project createdProject = requests.<Project>getRequest(PROJECTS).create(project);
        if (project.getId() == null) {
            project.setId(createdProject.getId());
        }
        return requests.<Project>getRequest(PROJECTS).read(project.getId());
    }

    protected Project createAndValidateProject(ChekedRequests requests, Consumer<Project> modifier, boolean full) {
        Project project = TestDataGenerator.generateProject(full);
        modifier.accept(project);
        Project createdProject = createProject(requests, project);
        if (project.getId() != null) {
            softy.assertEquals(project.getId(), createdProject.getId(), "Project id is not correct");
        }
        return createdProject;
    }

    protected Response createProjectNegative(ChekedRequests requests, boolean full, java.util.function.Consumer<Project> modifier) {
        Project project = TestDataGenerator.generateProject(full);
        modifier.accept(project);
        UncheckedBase unchecked = new UncheckedBase(requests.getSpec(), Endpoint.PROJECTS);
        return unchecked.create(project);
    }

    protected Response sendRequestWithoutBody(ChekedRequests requests, String body) {
        return io.restassured.RestAssured
                .given()
                .spec(requests.getSpec())
                .body(body)
                .post(Endpoint.PROJECTS.getUrl());
    }
}