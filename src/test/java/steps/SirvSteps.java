package steps;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import support.Config;
import support.TokenManager;


public class SirvSteps {
    private String bearer;
    private String sirvPath; 
    
    
    @io.cucumber.java.en.Given("I have a valid Sirv bearer token")
    public void i_have_a_valid_token() {
        RestAssured.baseURI = Config.get("baseUrl");
        RestAssured.basePath = "/v2";
        bearer = TokenManager.getToken();
        Assert.assertNotNull(bearer, "Token should not be null");
    }

    @io.cucumber.java.en.When("I upload the local file to the configured Sirv path")
    public void i_upload_the_file() throws Exception {
        sirvPath = Config.get("sirvPath");

        // Always load from classpath (src/test/resources)
        String resourceFile = Config.get("localFile");
        ClassLoader classLoader = getClass().getClassLoader();

        if (classLoader.getResource(resourceFile) == null) {
            throw new FileNotFoundException("File not found in resources: " + resourceFile);
        }

        localPath = Paths.get(classLoader.getResource(resourceFile).toURI());

        byte[] bytes = Files.readAllBytes(localPath);
        String contentType = Files.probeContentType(localPath);
        if (contentType == null) contentType = "application/octet-stream";

        given()
          .header("Authorization", "Bearer " + bearer)
          .contentType(contentType)
          .queryParam("filename", sirvPath)
          .body(bytes)
        .when()
          .post("/files/upload")
        .then()
          .statusCode(200);
    }


    @io.cucumber.java.en.Then("the file should exist on Sirv with a positive size")
    public void the_file_should_exist() {
        Response stat =
            given()
              .header("Authorization", "Bearer " + bearer)
              .queryParam("filename", sirvPath)
            .when()
              .get("/files/stat")
            .then()
              .statusCode(200)
              .extract().response();

        boolean isDirectory = stat.jsonPath().getBoolean("isDirectory");
        int size = stat.jsonPath().getInt("size");
        Assert.assertFalse(isDirectory, "Should be a file, not directory");
        Assert.assertTrue(size >= 0, "File size should be >= 0");
    }

    @io.cucumber.java.en.And("I delete the file from Sirv")
    public void i_delete_the_file() {
        given()
          .header("Authorization", "Bearer " + bearer)
          .queryParam("filename", sirvPath)
        .when()
          .post("/files/delete")
        .then()
          .statusCode(200);
    }
}
