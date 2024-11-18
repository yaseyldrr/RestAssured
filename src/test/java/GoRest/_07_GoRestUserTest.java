package GoRest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class _07_GoRestUserTest {

    RequestSpecification reqSpec;
    Faker randomGenerator = new Faker();
    int userID;

    @BeforeClass
    public void SetUp() {
        baseURI = "https://gorest.co.in/public/v2/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization",
                        "Bearer 053a18bb5f07a7a0b9f7fe8496cd893f864f54cd51cb848bf212df55da10d7ce")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void CreateUser() {
        // body hazırla
        /*
        {
    "name":"{{$randomFullName}}",
    "gender":"female",
    "email":"{{$randomEmail}}",
    "status":"active"
    }
         */
        String rndFullName = randomGenerator.name().fullName();
        String rndMail = randomGenerator.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullName);
        newUser.put("gender", "female");
        newUser.put("email", rndMail);
        newUser.put("status", "active");

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)

                        .when()
                        .post("users") // http ile başlamıyorsa baseURI geliyor

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println(userID);

    }

    @Test(dependsOnMethods = "CreateUser")
    public void GetUserByID() {
        given()
                .spec(reqSpec)
                .log().uri()
                //.body(newUser)

                .when()
                .get("users/" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
        ;
        System.out.println(userID);

    }

    @Test(dependsOnMethods = "GetUserByID") // tüm class çalışmalı
    public void UpdateUser() {
        /*
        {
    "name":"Yasemin Yıldırır",
    "email":"{{$randomEmail}}"
 }
         */
        // Güncellenen kulanıcı verisi
        String rndMail = randomGenerator.internet().emailAddress();

        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "Yasemin Yıldırır");
        updateUser.put("email", rndMail);

        given()
                .spec(reqSpec) // base uri ve gerekli header bilgileri burada
                .body(updateUser) // güncellenmiş kullanıcı verisini gövdeye ekler
                .log().uri() // istek uri sini loglar

                .when()
                .put("users/" + userID) // dinamik kullanucı id siyle endpoint tamamlanır

                .then()
                .log().body() // yanıt gövdesini loglar
                .statusCode(200) // beklenen http yanıt kodu doğrulaması
                .body("id", equalTo(userID))
                .body("email", equalTo(rndMail))
        ;

    }

    @Test(dependsOnMethods = "UpdateUser")
    public void DeleteUser() {
        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .delete("users/" + userID)

                .then()
                .log().body()
                .statusCode(204)
        ;

    }

    @Test(dependsOnMethods = "DeleteUser")
    public void DeleteUserNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("users/" + userID)

                .then()
                .statusCode(404)
        ;

    }

}
