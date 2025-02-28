package com.example.teamcity.projectTest;

import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.checked.ChekedRequests;
import com.example.teamcity.enums.Endpoint;
import org.testng.annotations.Test;

public class ProjectPositiveTest extends BaseTest {

    @Test(description = "Создание проекта с минимальным набором обязательных параметров")
    public void userCreatesProjectWithMinimalDataTest() {
        ChekedRequests userCheckRequests = createUser();
        Project createdProject = createAndValidateProject(userCheckRequests, project -> {}, false);
        softy.assertTrue(createdProject.getId() != null && !createdProject.getId().isEmpty(), "Project id is null or empty");
    }

    @Test(description = "Создание проекта с максимальным набором параметров")
    public void userCreatesProjectWithMaximalDataTest() {
        ChekedRequests userCheckRequests = createUser();
        Project createdProject = createAndValidateProject(userCheckRequests, project -> {}, true);
        softy.assertTrue(createdProject.getId() != null && !createdProject.getId().isEmpty(), "Project id is null or empty");
    }

    @Test(description = "Копирование проекта с добавлением sourceProject locator")
    public void userCopyProjectWithSourceProjectLocatorTest() {
        ChekedRequests userCheckRequests = createUser();
        Project mainProject = createAndValidateProject(userCheckRequests, project -> {}, false);
        Project copiedProject = createAndValidateProject(userCheckRequests, project -> {
            project.setSourceProject(new SourceProject(mainProject.getId()));}, true);

        softy.assertTrue(copiedProject.getId() != null && !copiedProject.getId().isEmpty(), "Project id is null or empty");
    }

    @Test(description = "Создание проекта с parentProject не _Root проекта")
    public void userCreatesProjectWithParentProjectLocatorTest() {
        ChekedRequests userCheckRequests = createUser();
        Project parentProject = createAndValidateProject(userCheckRequests, project -> {}, false);

        Project childProject = createAndValidateProject(userCheckRequests, project -> {
            project.setParentProject(new ParentProject(parentProject.getId()));}, false);

        Project createdChildProject = userCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read(childProject.getId());
        softy.assertEquals(parentProject.getId(), createdChildProject.getParentProjectId(), "Child parentProject id is not correct");
    }

    @Test(description = "Создание проекта с положением флага copyAllAssociatedSettings=false")
    public void userCreatesProjectWithFalseParameterCopyAllAssociatedSettingsTest() {
        ChekedRequests userCheckRequests = createUser();
        Project createdProject = createAndValidateProject(userCheckRequests, project -> {
            project.setCopyAllAssociatedSettings(false);}, false);

        softy.assertTrue(createdProject.getId() != null && !createdProject.getId().isEmpty(), "Project id is null or empty");
    }

    @Test(description = "Создание проекта без Id")
    public void userCreatesProjectWithoutIdTest() {
        ChekedRequests userCheckRequests = createUser();
        Project createdProject = createAndValidateProject(userCheckRequests, project -> {
            project.setId(null);}, false);

        String generatedId = createdProject.getId();
        softy.assertTrue(generatedId != null && !generatedId.isEmpty(), "Project id is null or empty");
    }
}