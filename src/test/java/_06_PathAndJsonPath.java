import Model.Location;
import Model.Place;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static javax.swing.UIManager.getInt;

public class _06_PathAndJsonPath {
    @Test
    public void extractingPath() {
        // gelen body de bilgiyi dışarı almanın 2 yöntemini gördük
        // .extract.path("")     ,   as(ToDo.Class)

        String postCode =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("'post code'");

        System.out.println("postCode = " + postCode);
        int postCodeInt = Integer.parseInt(postCode);
        System.out.println("postCodeInt = " + postCodeInt);
    }

    @Test
    public void extractingJsonPath() {
        // gelen body de bilgiyi dışarı almanın 2 yöntemini gördük
        // .extract.path("")     ,   as(ToDo.Class)

        int postCode =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'")
                // tip dönüşümü otomatik
                ;

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractingJsonPathInnerObject() {

        Response data =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .extract().response();
        Location locationAsPathObject = data.as(Location.class); // bütün class yapısını almak zorundayız
        //System.out.println("locationAsPathNesnesi = " + locationAsPathNesnesi);
        // bana sadece place dizisi lazım olsa bile, bütün diğer class ları yazmak zorundayım

        System.out.println("locationAsPathObject = " + locationAsPathObject);

        // Eğer bana içerdeki bir nesne tipli parçayı (places) almak isteseydim
        List<Place> places = data.jsonPath().getList("places", Place.class);

        // Sadece Place dizisi lazım ise diğerlerini yazmak zorunda değilsin.

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.

        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ile veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }

    @Test
    public void getUsersV1() {
        // https://gorest.co.in/public/v1/users  endpointte dönen Sadece Data Kısmını POJO
        // dönüşümü ile alarak yazdırınız.


    }


}
