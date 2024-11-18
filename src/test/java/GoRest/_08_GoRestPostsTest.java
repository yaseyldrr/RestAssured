package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class _08_GoRestPostsTest {
    RequestSpecification reqSpec;
    Faker randomGenerator = new Faker();
    int postID;

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
    public void PostList() {


        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .get("posts")

                .then()
                .log().body()
                .statusCode(200)
        ;
        System.out.println(postID);

    }

    @Test(dependsOnMethods = "PostList")
    public void PostCreate() {
        /*
        {

        "user_id": 7516730,
        "title": "{{$randomLoremSentence}}",
        "body": "{{$randomLoremParagraph}}"
}
         */
        String title = randomGenerator.lorem().sentence();
        String body = randomGenerator.lorem().paragraph();

        Map<String, Object> newPost = new HashMap<>();
        newPost.put("user_id","7530342");
        newPost.put("title", title);
        newPost.put("body", body);

        postID = given()
                .spec(reqSpec)
                .body(newPost)
                .log().uri()
                .log().body()

                .when()
                .post("posts")

                .then()
                .log().body()
                .statusCode(201)
                .body("title", equalTo(title))
                .body("body", equalTo(body))
                .body("user_id", equalTo(7530342))
                .extract()
                .path("id");
        ;
        ;
        System.out.println(postID);

    }

    @Test(dependsOnMethods = "PostCreate")
    public void UpdatePost() {
        /*
        {

        "title": "Bugün hava durumu",
        "body": "İngiltere'de havalar artık soğuk, 7-10 derece"
}
         */
        // Güncellenen kulanıcı verisi
        String title = "Bugün hava çok güzel";
        String body = "İngiltere'de havalar artık soğuk, 7-10 derece";

        Map<String, String> updatePost = new HashMap<>();
        updatePost.put("title", title);
        updatePost.put("body", body);

        given()
                .spec(reqSpec)
                .body(updatePost)
                .log().uri()

                .when()
                .put("posts/" + postID)

                .then()
                .log().body()
                .statusCode(200)
                .body("title", equalTo(title))
                .body("body", equalTo(body))
        ;

    }

    @Test(dependsOnMethods = "UpdatePost")
    public void DeletePost() {
        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .delete("posts/" + postID)

                .then()
                .log().body()
                .statusCode(204)
        ;

    }

    @Test(dependsOnMethods = "DeletePost")
    public void DeletePostNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("posts/" + postID)

                .then()
                .statusCode(404)
        ;

    }
}
