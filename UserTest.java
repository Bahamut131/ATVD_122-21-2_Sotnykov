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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class UserTest {

    private static final String baseUrl = "https://petstore.swagger.io/v2";

    private static final String USER = "/user",
            USER_USERNAME = USER + "/{username}",
            USER_LOGIN = USER + "/login",
            USER_LOGOUT = USER + "/logout";


    private String username;
    private String firstName;


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    @Test(dependsOnMethods = "verifyLoginAction")
    public void verifyLoginAction() {


        Map<String, ?> body = Map.of(
                "username", "OlekseySotnikov",
                "password", "122-21-2k"
        );

        Response response = given().body(body).get(USER_LOGIN);

        response.then().statusCode(HttpStatus.SC_OK);

        RestAssured.requestSpecification
                .sessionId(response.jsonPath()
                        .get("message").toString()
                        .replaceAll("[^0-9]", ""));
    }


    @Test(dependsOnMethods = "verifyCreateAction")
    public void verifyCreateAction() {

        username = Faker.instance().name().username();
        firstName = Faker.instance().name().firstName();


        Map<String, ?> body = Map.of(
                "username", username,
                "firstName", firstName,
                "lastName", Faker.instance().gameOfThrones().character(),
                "email", Faker.instance().internet().emailAddress(),
                "password", Faker.instance().internet().password(),
                "phone", Faker.instance().phoneNumber().phoneNumber(),
                "userStatus", Integer.valueOf("1")

        );

        given().body(body).post(USER).then().statusCode(HttpStatus.SC_OK);

    }



    @Test(dependsOnMethods = "verifyGetAction")
    public void verifyGetAction() {
        given().pathParams("username", username)
                .get(USER_USERNAME)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("firstName", equalTo(firstName));
    }


    @Test(dependsOnMethods = "verifyDeleteAction")
    public void verifyDeleteAction() {
        given().pathParams("username", username)
                .delete(USER_USERNAME)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyLogoutAction", priority = 1)
    public void verifyLogoutAction() {
        given().get(USER_LOGOUT)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}
