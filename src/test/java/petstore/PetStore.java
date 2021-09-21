package petstore;

import org.testng.annotations.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;


public class PetStore {
    String uri = "https://petstore.swagger.io/v2/pet";
    String idPet = "1001";

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("src/test/resources/db/pet1.json");
        given() //Dado
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .post(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Toto"))
                .body("status", is("available"))
                .body("category.name", is("dog"))
                .body("tags.name", contains("Animal")) //lista
        ;
    }

    @Test
    public void consultarPet(){

        String token =
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/" + idPet)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Toto"))
                .body("category.name", is("dog"))
                .body("tags.name", contains("Animal"))
        .extract()
                .path("category.name")
        ;
        System.out.printf("O token e %s.",token);
    }

    @Test
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("src/test/resources/db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Toto"))
                .body("status", is("sold"))
        ;
    }

    @Test
    public void excluirPet(){
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(uri + "/" + idPet)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("message", is(idPet))
        ;
    }
}
