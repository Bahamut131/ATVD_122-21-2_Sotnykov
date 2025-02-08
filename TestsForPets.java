package org.example.lab3;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;

import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
public class TestsForPets {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String PET = "/pet";
    private static final String PET_BY_ID = PET + "/{petId}";

    private final int petId = 122212;
    private final String petName = "SotnikovOM";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    @Test(dependsOnMethods = "testCreatePet")
    public void testCreatePet() {
        Map<String, ?> body = Map.of(
                "id", petId,
                "name", petName,
                "status", "available"
        );

        given().body(body).post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo(petId))
                .body("name", equalTo(petName));
    }

    @Test(dependsOnMethods = "testCreatePet")
    public void testGetPetById() {
        given().pathParams("petId", petId)
                .get(PET_BY_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo(petId))
                .body("name", equalTo(petName));
    }

    @Test(dependsOnMethods = "testGetPetById")
    public void testUpdatePet() {
        String updatedName = petName + "_Updated";

        Map<String, ?> body = Map.of(
                "id", petId,
                "name", updatedName,
                "status", "sold"
        );

        given().body(body).put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo(petId))
                .body("name", equalTo(updatedName))
                .body("status", equalTo("sold"));
    }

    @Test(dependsOnMethods = "testUpdatePet")
    public void testDeletePet() {
        given().pathParams("petId", petId)
                .delete(PET_BY_ID)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
