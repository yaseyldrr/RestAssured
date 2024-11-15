import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;


public class _03_APITestExtract {

    @Test
    public void extractingJsonPath() {

        String countryName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("country") // path i country olan değeri extract yap
                ;
        System.out.println(countryName);
        Assert.assertEquals(countryName, "United States");


    }

    @Test
    public void extractingJsonPath2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint inde dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız

        String state =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].state");
        System.out.println(state);
        Assert.assertEquals(state, "California"); // TestNG Assertion

    }

    @Test
    public void extractingJsonPath3() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // limit bilgisinin 10 olduğunu testNG ile doğrulayınız.

        int limit =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("meta.pagination.limit");
        System.out.println(limit);
        Assert.assertTrue(limit == 10);
    }

    @Test
    public void extractingJsonPath4() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // data dakibütün id leri nasıl alırız ?

        List<Integer> ids =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.id");
        System.out.println(ids);
    }

    @Test
    public void extractingJsonPath5() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // bütün name leri yazdırınız

        List<String> names =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.name");

        for (String s : names) {
            System.out.println("name: " + s);
        }
    }

    @Test
    public void extractingJsonPathResponseAll() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // bütün name leri yazdırınız

        Response data =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().response();

        List<Integer> ids = data.path("data.id");
        List<String> names = data.path("data.name");
        int limit = data.path("meta.pagination.limit");

        System.out.println(ids);
        System.out.println(names);
        System.out.println(limit);

        Assert.assertTrue(ids.contains(7520559));
        Assert.assertTrue(names.contains("Chitrangada Embranthiri"));
        Assert.assertTrue(limit == 10);
    }


}
