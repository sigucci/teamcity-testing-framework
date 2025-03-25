package com.example.teamcity.buildTypeUiTest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.ui.BaseUiTest;
import com.example.teamcity.ui.pages.admin.CreateBuildPage;
import com.example.teamcity.ui.pages.admin.ProjectPage;
import com.example.teamcity.ui.pages.admin.ProjectsPage;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Selenide.$;
import static com.example.teamcity.ui.pages.admin.ProjectPage.BuildName;

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

        //Выявил, что тест иногда падает после создания проекта, потому что не может найти строку ввода url после загрузки страницы.
        // Небольшой задержкой эта проблема полностью решается.
        Selenide.sleep(2000);

        CreateBuildPage.open(createdProject.getId())
                .createBuildForm(REPO_URL)
                .setupBuild(buildTypeName)
                .submit();

        new CreateBuildPage().submitAnywayButton();

        SelenideElement errorMessage = new CreateBuildPage().getErrorMessage();

        softy.assertTrue(errorMessage.isDisplayed());
    }
}
