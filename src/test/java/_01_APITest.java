import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;

public class _01_APITest {
    @Test
    public void Test1() {
        // 1- url çağırmadan önce hazırlıkların yapıldığı bölüm : Request, gidecek body
        //                                                     token
        // 2- endpoint in çağrıldığı bölüm : Endpoint in çağrılması(metod: get, post vs)
        // 3- endpoint çağrıldıktan sonraki bölüm : Respose, Test(Assertion), Data


        given().
                // 1. bölüm işleri: giden body, token

                        when().
                // 2. bölüm işleri: metod, endpoint

                        then();
        // 3. bölüm işleri: gelen data, assert, test

    }

    @Test
    public void statusCodeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // dönen data
                // log all : bütün bilgiler
                .statusCode(200)
        ;
    }

    @Test
    public void contentTypeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // dönen data
                // log all : bütün bilgiler
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void checkCountryInResponseBody() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // dönen data
                // log all : bütün bilgiler
                .contentType(ContentType.JSON)
                .body("country", equalTo("United States")) // Assertion
        // country yi dışarı almadan bulunduğu yeri (path i) vererek içerde assertion hamcrest kütüphanesi yapıyor
        // pm.collectionVariables.set("UlkeAdi", pm.response.json().name);
        // pm.collectionVariables.set("UlkeKodu", pm.response.json().code);
        ;
    }

    @Test
    public void checkHasItem() {

        given()
                // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
                // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
                // olduğunu doğrulayınız
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))
        //places içindeki bütün place name ler in
        // içinde Dörtağaç Köyü var mı
        ;
    }

    @Test
    public void checkCountryInResponseBody2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin ilk elemanında  "California" değerinin
        // olduğunu doğrulayınız

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state", equalTo("California"))

        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places", hasSize(1))
        ;
    }

    @Test
    public void combiningTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places", hasSize(1)) // places in eleman uzunluğuı 1 mi
                .body("places[0].state", equalTo("California"))
                .body("places.'place name'", hasItem("Beverly Hills"))
        // Yukarıda olduğu gibi istenilen kadar test eklenebilir.
        ;
    }

    @Test
    public void pathParamTest() {

        given()
                .pathParam("ulke", "us")
                .pathParam("postaKodu", 90210)
                .log().uri() // oluşacak endpoint

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKodu}") // path parameter

                .then()
                .log().all()
        ;
    }

    /*
    https://sonuc.osym.gov.tr/Sorgu.aspx?SonucID=9917
    Query Parameter yöntemi ? yani sorgu, query


    https://gorest.co.in/public/v2/users/6942409
    Path Parameter yöntemi   yani path ini yani yolunu
     */
    @Test
    public void queryParamTest() {
        // https://gorest.co.in/public/v1/users?page=3
        given()
                .param("page", 3)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users") // URL ile URI ayrıldı


                .then()
                .log().body()
        ;

    }

    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.
        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")


                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }

}
