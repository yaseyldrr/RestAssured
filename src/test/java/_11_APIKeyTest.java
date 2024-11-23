import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

// enpoint :"https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user"
// Key : "x-api-key"   -> Bearer
// Values : "GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra"   -> token

public class _11_APIKeyTest {

    @Test
    public void Test1()
    {
        given()
                .header("x-api-key", "GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra")

                .when()
                .get("https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user")

                .then()
                .log().body()
        ;

    }
}
