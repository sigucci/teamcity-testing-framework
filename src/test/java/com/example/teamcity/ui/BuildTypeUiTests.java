package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.ui.pages.admin.CreateBuildPage;
import com.example.teamcity.ui.pages.admin.ProjectPage;
import com.example.teamcity.ui.pages.admin.ProjectsPage;
import org.testng.annotations.Test;
import static com.example.teamcity.ui.pages.admin.ProjectPage.BuildName;

@Test(groups = {"Regression"})
public class BuildTypeUiTests extends BaseUiTest {

    private static final String REPO_URL = "https://github.com/AlexPshe/spring-core-for-qa";

    @Test(description = "Создание билда")
    public void userCreatesBuildType() {
        var createdProject = superUserChekedRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());

        CreateBuildPage.open(createdProject.getId())
                .createBuildForm(REPO_URL)
                .setupBuild(testData.getBuildType().getName());

        new CreateBuildPage().submit();

        ProjectPage.open(createdProject.getId());
        ProjectsPage.open().getProjects();

        ProjectsPage.open()
                .getProjectByName(testData.getProject().getName())
                .getBuildName(testData.getBuildType().getName());

        softy.assertEquals(
                BuildName.findBy(Condition.text(testData.getBuildType().getName()))
                        .shouldBe(Condition.visible)
                        .text(),
                testData.getBuildType().getName()
        );
    }

    @Test(description = "Создание двух билдов с одинаковыми названиями")
    public void userCanNotCreateTwoBuildTypesWithSameId() {
        var createdProject = superUserChekedRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());

        CreateBuildPage.open(createdProject.getId())
                .createBuildForm(REPO_URL)
                .setupBuild(testData.getBuildType().getName());

        String buildTypeName = CreateBuildPage.getBuildName();

        new CreateBuildPage().submit();

        Selenide.sleep(2000);

        CreateBuildPage.open(createdProject.getId())
                .createBuildForm(REPO_URL)
                .setupBuild(buildTypeName)
                .submit();

        new CreateBuildPage().submitAnywayButton();

        SelenideElement errorMessage = new CreateBuildPage().getErrorMessage();

        softy.assertTrue(errorMessage.isDisplayed());
    }

    @Test(description = "Поиск проекта по имени")
    public void userCanSearchProjectByName() {
        var createdProject = superUserChekedRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());

        ProjectsPage projectsPage = ProjectsPage.open()
                .searchProjectInput(createdProject.getName());

        String foundProjectName = projectsPage
                .getSearchedProjectByName(createdProject.getName())
                .text();

        softy.assertEquals(foundProjectName, createdProject.getName());
    }
}
