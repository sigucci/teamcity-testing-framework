package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class CreateBuildPage extends CreateBasePage {

    private static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";

    private SelenideElement buildTypeNameInput = $("#buildTypeName");

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
}

//http://localhost:8111/admin/createObjectMenu.html?projectId=SpringCoreForQa&showMode=createBuildTypeMenu#createFromUrl