package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class _10_CitizenshipTest extends CampusParent {

    @Test
    public void CreateCitizenship() {
        name = randomGenerator.address().country() + randomGenerator.number().digits(5);
        shortName = randomGenerator.address().countryCode() + randomGenerator.number().digits(5);

        Map<String, String> createCitizen = new HashMap<>();
        createCitizen.put("name", name);
        createCitizen.put("shortName", shortName);

        citizenID =
                given()
                        .spec(reqSpec)
                        .body(createCitizen)

                        .when()
                        .post("school-service/api/citizenships")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }
    @Test(dependsOnMethods = "CreateCitizenship")
    public void CreateCitizenshipNegative() {

        Map<String, String> createCitizen = new HashMap<>();
        createCitizen.put("name", name);
        createCitizen.put("shortName", shortName);


        given()
                .spec(reqSpec)
                .body(createCitizen)

                .when()
                .post("school-service/api/citizenships")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsStringIgnoringCase("already"));

    }
    @Test(dependsOnMethods = "CreateCitizenshipNegative")
    public void UpdateCitizenship ()
    {
        name = "yase country 1" + randomGenerator.number().digits(5);
        shortName = "12345";
        Map<String, String> updateCountry = new HashMap<>();
        updateCountry.put("id", citizenID);
        updateCountry.put("name", name);
        updateCountry.put("shortName", shortName);

        given()
                .spec(reqSpec)
                .body(updateCountry)

                .when()
                .put("school-service/api/citizenships")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("shortName", equalTo(shortName));

    }
    @Test(dependsOnMethods = "UpdateCitizenship")
    public void DeleteCitizenship() {
        given()
                .spec(reqSpec)
                .pathParam("citizenID",citizenID)
                .log().uri()

                .when()
                .delete("school-service/api/citizenships/{citizenID}")

                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "DeleteCitizenship")
    public void DeleteCitizenshipNegative() {
        given()
                .spec(reqSpec)
                .pathParam("citizenID",citizenID)
                .log().uri()

                .when()
                .delete("school-service/api/citizenships/{citizenID}")

                .then()
                .log().body()
                .statusCode(400);
    }
}
