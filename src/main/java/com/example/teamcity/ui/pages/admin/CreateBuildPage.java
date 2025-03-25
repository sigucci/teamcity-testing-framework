package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreateBuildPage extends CreateBasePage {

    private static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";

    private static final SelenideElement buildTypeNameInput = $("#buildTypeName");

    public static CreateBuildPage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_TYPE_SHOW_MODE), CreateBuildPage.class);
    }

    public CreateBuildPage createBuildForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public CreateBuildPage setupBuild(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        return this;
    }

    public void submit() {
        submitButton.click();
    }

    public static String getBuildName() {
        return $("#buildTypeName").shouldBe(Condition.visible).getValue();
    }

    public CreateBuildPage submitAnywayButton() {
        SelenideElement submitAnyway = $("input[value='Create Duplicate VCS Root']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5));
        if (submitAnyway.is(Condition.visible)) {
            submitAnyway.click();
        }
        return this;
    }

    public SelenideElement getErrorMessage() {
        return $("#error_buildTypeName").shouldBe(Condition.visible);
    }
}