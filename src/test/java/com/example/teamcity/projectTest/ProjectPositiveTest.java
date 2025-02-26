package com.example.teamcity.projectTest;

import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.checked.ChekedRequests;
import org.testng.annotations.Test;
import static com.example.teamcity.enums.Endpoint.*;
import static org.testng.AssertJUnit.assertTrue;


public class ProjectPositiveTest extends BaseTest {

    @Test(description = "Создание проекта с минимальным набором обязательных параметров")
    public void userCreatesProjectWithMinimalDataTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(false);
        Project createdProject = createProject(userCheckRequests, project);

        softy.assertEquals(project.getId(), createdProject.getId(), "Project id is not correct");
    }

    @Test(description = "Создание проекта с максимальным набором параметров")
    public void userCreatesProjectWithMaximalDataTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(true);
        Project createdProject = createProject(userCheckRequests, project);

        softy.assertEquals(project.getId(), createdProject.getId(), "Project id is not correct");
    }

    @Test(description = "Копирование проекта с добавлением sourceProject locator")
    public void userCopyProjectWithSourceProjectLocatorTest() {
        ChekedRequests userCheckRequests = createUser();

        Project mainProject = TestDataGenerator.generateProject(false);
        createProject(userCheckRequests, mainProject);

        Project copiedProject = TestDataGenerator.generateProject(true);
        copiedProject.setSourceProject(new SourceProject(mainProject.getId()));
        createProject(userCheckRequests, copiedProject);

        softy.assertEquals(mainProject.getId(), copiedProject.getSourceProject().getLocator(), "Copied sourceProject id is not correct");
    }

    @Test(description = "Создание проекта с parentProject не _Root проекта")
    public void userCreatesProjectWithParentProjectLocatorTest() {
        ChekedRequests userCheckRequests = createUser();

        Project parentProject = TestDataGenerator.generateProject(false);
        createProject(userCheckRequests, parentProject);

        Project childProject = TestDataGenerator.generateProject(false);
        childProject.setParentProject(new ParentProject(parentProject.getId()));
        createProject(userCheckRequests, childProject);

        softy.assertEquals(childProject.getParentProject().getLocator(), parentProject.getId(), "Child parentProject id is not correct in created request");

        Project createdChildProject = userCheckRequests.<Project>getRequest(PROJECTS).read(childProject.getId());
        softy.assertEquals(parentProject.getId(), createdChildProject.getParentProjectId(), "Child parentProject id is not correct in returned response");
    }

    @Test(description = "Создание проекта с положением флага copyAllAssociatedSettings=false")
    public void userCreatesProjectWithFalseParameterCopyAllAssociatedSettingsTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(false);
        project.setCopyAllAssociatedSettings(false);

        Project createdProject = createProject(userCheckRequests, project);
        softy.assertEquals(project.getId(), createdProject.getId(), "Project id is not correct");
    }

    @Test(description = "Создание проекта без Id")
    public void userCreatesProjectWithoutIdTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(false);
        project.setId(null);
        Project createdProject = createProject(userCheckRequests, project);

        String generatedId = createdProject.getId();;
        softy.assertTrue(generatedId != null && !generatedId.isEmpty(), "Project id is null or empty");
    }
}