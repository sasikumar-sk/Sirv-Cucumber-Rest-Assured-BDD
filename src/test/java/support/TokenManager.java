package support;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType; 

import io.restassured.response.Response;


public class TokenManager {
    private static String token;
    private static Instant expiry;

    public static synchronized String getToken() {
        if (token == null || Instant.now().isAfter(expiry)) {
            String baseUrl = Config.get("baseUrl");
            String clientId = Config.get("clientId");
            String clientSecret = Config.get("clientSecret");

            Map<String, String> body = new HashMap<>();
            body.put("clientId", clientId);
            body.put("clientSecret", clientSecret);

            Response res = RestAssured.given()
                    .baseUri(baseUrl)
                    .basePath("/v2")
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post("/token")
                    .then()
                    .statusCode(200)
                    .extract().response();

            token = res.jsonPath().getString("token");
            int expiresIn = res.jsonPath().getInt("expiresIn"); // seconds (usually 1200)
            expiry = Instant.now().plusSeconds(expiresIn - 60); // refresh 1 min early
        }
        return token;
    }
}
