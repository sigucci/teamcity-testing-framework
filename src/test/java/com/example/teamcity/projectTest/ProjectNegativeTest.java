package com.example.teamcity.projectTest;

import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.models.ParentProject;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.SourceProject;
import com.example.teamcity.api.requests.checked.ChekedRequests;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class ProjectNegativeTest extends BaseTest {

    @Test(description = "Создание проекта без Name")
    public void userCanNotCreateProjectWithoutNameTest() {
        ChekedRequests userCheckRequests = createUser();
        Response response = createProjectNegative(userCheckRequests, false, project -> project.setName(null));
        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status code was not 400 Bad Request");
    }

    @Test(description = "Создание проекта с пустым телом запроса")
    public void userCanNotCreateProjectWithEmptyBodyTest() {
        ChekedRequests userCheckRequests = createUser();
        Response response = sendRequestWithoutBody(userCheckRequests, "null");
        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_INTERNAL_SERVER_ERROR, "Status code was not 500 Internal Server Error");
    }

    @Test(description = "Создание проекта с неваоидным JSON")
    public void userCanNotCreateProjectWithInvalidJsonTest() throws JsonProcessingException {
        ChekedRequests userCheckRequests = createUser();
        Project project = TestDataGenerator.generateProject(false);

        ObjectMapper mapper = new ObjectMapper();
        String validJson = mapper.writeValueAsString(project);
        String invalidJson = validJson.substring(0, validJson.length() - 1);

        Response response = sendRequestWithoutBody(userCheckRequests, invalidJson);

        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status code was not 400 Bad Request");
    }

    @Test(description = "Создание проекта с дублированным id")
    public void userCanNotCreateProjectWithDuplicatedIdTest() {
        ChekedRequests userCheckRequests = createUser();

        Project project = TestDataGenerator.generateProject(false);
        Project createdProject = createProject(userCheckRequests, project);

        Response response = createProjectNegative(userCheckRequests, false, proj -> proj.setId(createdProject.getId()));
        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status code was not 400 Bad Request");
    }

    @Test(description = "Создание проекта с несуществующим parentProject")
    public void userCanNotCreateProjectWithNotExistingParentProjectTest() {
        ChekedRequests userCheckRequests = createUser();
        Response response = createProjectNegative(userCheckRequests, true, project ->
                project.setParentProject(new ParentProject("test_not_existing_parentProject"))
        );
        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Status code was not 404 Not Found");
    }

    @Test(description = "Создание проекта с несуществующим sourceProject должно вернуть 404 Not Found")
    public void userCanNotCreateProjectWithNotExistingSourceProjectTest() {
        ChekedRequests userCheckRequests = createUser();
        Response response = createProjectNegative(userCheckRequests, true, project ->
                project.setSourceProject(new SourceProject("test_not_existing_sourceProject"))
        );
        softy.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Status code was not 404 Not Found");
    }
}