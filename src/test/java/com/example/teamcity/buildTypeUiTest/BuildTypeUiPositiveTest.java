package com.example.teamcity.buildTypeUiTest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.ui.BaseUiTest;
import com.example.teamcity.ui.pages.admin.CreateBuildPage;
import com.example.teamcity.ui.pages.admin.ProjectPage;
import com.example.teamcity.ui.pages.admin.ProjectsPage;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuildTypeUiPositiveTest extends BaseUiTest {

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

        SelenideElement project = $$("span.ProjectsTreeItem__name--uT")
                .findBy(Condition.text(testData.getProject().getName()));
        project.click();

        SelenideElement build = $$("span.MiddleEllipsis__searchable--uZ")
                .findBy(Condition.text(testData.getBuildType().getName()));
        build.shouldBe(Condition.visible);

        softy.assertEquals(build.text(), testData.getBuildType().getName());
    }

    @Test(description = "Создание двух билдов с одинаковыми названиями")
    public void userCanNotCreateTwoBuildTypesWithSameId() {
        var createdProject = superUserChekedRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());

        CreateBuildPage.open(createdProject.getId())
                .createBuildForm(REPO_URL)
                .setupBuild(testData.getBuildType().getName());

        String buildTypeName = $("#buildTypeName").shouldBe(Condition.visible).getValue();

        new CreateBuildPage().submit();

        CreateBuildPage.open(createdProject.getId())
                .createBuildForm(REPO_URL)
                .setupBuild(buildTypeName);

        new CreateBuildPage().submit();

        SelenideElement submitAnywayButton = $("#submitAnywayButton")
                .shouldBe(Condition.visible);
        submitAnywayButton.click();

        SelenideElement errorMessage = $("#error_buildTypeName")
                .shouldBe(Condition.visible);
    }
}
