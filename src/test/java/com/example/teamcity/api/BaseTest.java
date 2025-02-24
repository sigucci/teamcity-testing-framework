package com.example.teamcity.api;

import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.generator.TestDataStorage;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.checked.ChekedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import static com.example.teamcity.api.generator.TestDataGenerator.generateAll;

public class BaseTest {
    protected SoftAssert softy;
    protected ChekedRequests superUserChekedRequests = new ChekedRequests(Specifications.superUserSpec());
    protected TestData testData;

    protected TestData getTestData() {
        boolean generateFullData = Boolean.parseBoolean(System.getProperty("generateFullData", "false"));
        return generateFullData ? TestDataGenerator.generateAll() : TestDataGenerator.generateWithoutExcluded();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        softy = new SoftAssert();
        testData = getTestData();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        softy.assertAll();
        TestDataStorage.getStorage().deleteCreatedEntities();
    }
}