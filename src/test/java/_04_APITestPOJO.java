import Model.Location;
import groovy.json.JsonToken;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class _04_APITestPOJO {
    @Test
    public void extractJsonAll_POJO() {

        Location locationObject =
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .extract().body().as(Location.class) // Tüm body yi class a göre çevir
        ;
        System.out.println(locationObject.getCountry());
        System.out.println(locationObject);
    }
}
