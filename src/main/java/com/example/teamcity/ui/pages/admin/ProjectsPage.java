package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.elements.ProjectElement;
import com.example.teamcity.ui.pages.BasePage;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProjectsPage extends BasePage {
    private static final String PROJECTS_URL = "/favorite/projects";

    private static final ElementsCollection projectElements = $$("div[class*='Subproject__container']");

    private static final SelenideElement spanFavoriteProjects = $("span[class='ProjectPageHeader__title--ih']");

    private static final SelenideElement header = $(".MainPanel__router--gF > div");

    private static final ElementsCollection getProjectByName = $$("span.ProjectsTreeItem__name--uT");

    public static ProjectsPage open() {
        return Selenide.open(PROJECTS_URL, ProjectsPage.class);
    }

    public ProjectsPage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }

    public List<ProjectElement> getProjects() {
        return generatePageElements(projectElements, ProjectElement::new);
    }

    public ProjectPage getProjectByName(String projectName) {
        getProjectByName.findBy(Condition.text(projectName))
                .shouldBe(Condition.visible)
                .click();

        return new ProjectPage();
    }
}
