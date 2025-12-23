package vir;

import java.net.URI; // 1. Added this import
import java.net.http.HttpClient; // Added for sending
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class spotifyAuth {
    private String token;
    String clientId = "40a576a154df4b3b856f58c0ce4712b0";
    String clientSecret = "9725df56fabf46838126025ae11cd808";

    String body= "grant_type=client_credentials&client_id="+clientId+"&client_secret="+clientSecret;

    public String getToken() throws Exception{
        return token;
    }

    public void setToken()  throws Exception{
        HttpRequest postRequest = HttpRequest.newBuilder()
              .uri(URI.create("https://accounts.spotify.com/api/token"))
              .header("Content-Type", "application/x-www-form-urlencoded")
              .POST(BodyPublishers.ofString(body))
              .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(postRequest, BodyHandlers.ofString());
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
        this.token = jsonObject.get("access_token").getAsString();
        
    }
}
