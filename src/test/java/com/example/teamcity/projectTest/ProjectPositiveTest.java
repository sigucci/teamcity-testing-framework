package com.example.teamcity.projectTest;

import com.example.teamcity.api.BaseApiTest;
import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.generator.RandomData;
import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.checked.ChekedRequests;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.enums.Endpoint.*;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ProjectPositiveTest extends BaseTest {
    @Test(description = "Создание проекта с минимальным набором обязательных параметров")
    public void userCreatesProjectWithMinimalDataTest() {
        superUserChekedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new ChekedRequests(Specifications.authSpec(testData.getUser()));

        Project project = TestDataGenerator.generateProject(false);
        userCheckRequests.<Project>getRequest(PROJECTS).create(project);

        var createdProject = userCheckRequests.<Project>getRequest(PROJECTS).read(project.getId());
        softy.assertEquals(project.getId(), createdProject.getId(), "Project id is not correct");
    }

    @Test(description = "Создание проекта с максимальным набором параметров")
    public void userCreatesProjectWithMaximalDataTest() {
        superUserChekedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new ChekedRequests(Specifications.authSpec(testData.getUser()));

        Project project = TestDataGenerator.generateProject(true);
        userCheckRequests.<Project>getRequest(PROJECTS).create(project);

        var createdProject = userCheckRequests.<Project>getRequest(PROJECTS).read(project.getId());
        softy.assertEquals(project.getId(), createdProject.getId(), "Project id is not correct");
    }

    @Test(description = "Копирование проекта с добавлением sourceProject locator")
    public void userCopyProjectWithSourceProjectLocatorTest() {
        superUserChekedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new ChekedRequests(Specifications.authSpec(testData.getUser()));

        Project mainProject = TestDataGenerator.generateProject(false);
        userCheckRequests.<Project>getRequest(PROJECTS).create(mainProject);

        Project copiedProject = TestDataGenerator.generateProject(true);
        copiedProject.setSourceProject(new SourceProject(mainProject.getId()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(copiedProject);

        softy.assertEquals(mainProject.getId(), copiedProject.getSourceProject().getLocator(), "Copied sourceProject id is not correct");
    }

    @Test(description = "Создание проекта с parentProject не _Root проекта")
    public void userCreatesProjectWithParentProjectLocatorTest() {
        superUserChekedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new ChekedRequests(Specifications.authSpec(testData.getUser()));

        Project parentProject = TestDataGenerator.generateProject(false);
        userCheckRequests.<Project>getRequest(PROJECTS).create(parentProject);

        Project childProject = TestDataGenerator.generateProject(false);
        childProject.setParentProject(new ParentProject(parentProject.getId()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(childProject);
        softy.assertEquals(childProject.getParentProject().getLocator(), parentProject.getId(), "Child parentProject id is not correct in created request");

        var createdChildProject = userCheckRequests.<Project>getRequest(PROJECTS).read(childProject.getId());
        softy.assertEquals(parentProject.getId(), createdChildProject.getParentProjectId(), "Child parentProject id is not correct in returned response");
    }
}