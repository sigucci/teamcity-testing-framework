package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import com.example.teamcity.ui.pages.admin.ProjectPage;
import com.example.teamcity.ui.pages.admin.ProjectsPage;
import org.testng.annotations.Test;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {

    private static final String REPO_URL = "https://github.com/AlexPshe/spring-core-for-qa";

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {

        loginAs(testData.getUser());

        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        var createdProject = superUserChekedRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);

        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText(testData.getProject().getName()));

        var foundProjects = ProjectsPage.open()
                .getProjects().stream()
                .anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));

        softy.assertTrue(foundProjects);

    }

    @Test(description = "User should not be able to craete project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        step("Login as user");
        step("Check number of projects");
        step("Open create project page (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send all project parameters (repository URL)");
        step("Click `Proceed`");
        step("Set Project Name");
        step("Click `Proceed`");
        step("Check that number of projects did not change");
        step("Check that error appears `Project name must not be empty`");
    }
}
