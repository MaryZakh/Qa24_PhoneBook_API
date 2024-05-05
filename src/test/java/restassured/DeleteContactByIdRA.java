package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIdRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyYUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcxNTE3NzQ5MiwiaWF0IjoxNzE0NTc3NDkyfQ.A1dSaWGAiBvnUzE6WR8jSR2gjp4rJ-T8QTZgCsICPLs";
    String id;

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Donna")
                .lastName("Doww")
                .email("donna"+i+"@gmail.com")
                .phone("1236598"+i)
                .address("Tel Aviv")
                .build();

        String message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");
        String[] all = message.split(": ");
        id = all[1];
    }

    @Test
    public void deleteContactById(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",equalTo("Contact was deleted!"));
    }

    @Test
    public void deleteContactByIdWrongToken(){
        given()
                .header("Authorization","hkhgn")
                .when()
                .delete("contacts/" +id)
                .then()
                .assertThat().statusCode(401);

    }
}
