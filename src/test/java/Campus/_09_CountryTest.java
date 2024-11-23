package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _09_CountryTest {
    Faker randomGenerator = new Faker();
    RequestSpecification reqSpec;
    String countryName = "";
    String countryCode = "";
    String countryID = "";

    @BeforeClass
    public void Setup() {
        baseURI = "https://test.mersys.io";

        Map<String, String> userCredential = new HashMap<>(); // Bir Map kullanılarak kullanıcı giriş bilgileri JSON formatında oluşturulur
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies = // Giriş API Çağrısı
                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when()
                        .post("/auth/login")

                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().detailedCookies(); // Yanıtın içinden oturum için kullanılan cookies (çerezleri) çıkarır.
        ;
        System.out.println("cookies = " + cookies);

        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies) // Daha önce alınan oturum çerezlerini bu isteklere ekler.
                .build();
    }

    @Test
    public void CreateCountry() { // Ülke Oluşturma API Çağrısı
        countryName = randomGenerator.address().country() + randomGenerator.number().digits(5);
        countryCode = randomGenerator.address().countryCode() + randomGenerator.number().digits(5);

        Map<String, String> createCountry = new HashMap<>();
        createCountry.put("name", countryName);
        createCountry.put("code", countryCode);

        countryID =
                given()
                        .spec(reqSpec)
                        .body(createCountry)

                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "CreateCountry")
    public void CreateCountryNegative() {

        Map<String, String> createCountry = new HashMap<>();
        createCountry.put("name", countryName);
        createCountry.put("code", countryCode);


        given()
                .spec(reqSpec)
                .body(createCountry)

                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsStringIgnoringCase("already"));
        // String responseBody = response.getBody().asString();
        // Assert.assertTrue(responseBody.contains("already exists"),
        // "Fail!");

    }

    @Test(dependsOnMethods = "CreateCountryNegative")
    public void UpdateCountry() {
        countryName = "yase country 1" + randomGenerator.number().digits(5);
        countryCode = "12345";
        Map<String, String> updateCountry = new HashMap<>();
        updateCountry.put("id", countryID);
        updateCountry.put("name", countryName);
        updateCountry.put("code", countryCode);

        given()
                .spec(reqSpec)
                .body(updateCountry)

                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(countryName))
                .body("code", equalTo(countryCode));

    }
    @Test(dependsOnMethods = "UpdateCountry")
    public void DeleteCountry() {
        given()
                .spec(reqSpec)
                .pathParam("countryID",countryID)
                .log().uri()

                .when()
                .delete("school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "DeleteCountry")
    public void DeleteCountryNegative() {
        given()
                .spec(reqSpec)
                .pathParam("countryID",countryID)
                .log().uri()

                .when()
                .delete("school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(400);
    }


}
