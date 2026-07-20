package vir;

import java.net.URI; 
import java.net.http.HttpClient; 
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;




import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;



public class spotifyPlaylist{

    class PlaylistResponse { 
        TracksContainer tracks;
    }
    class TracksContainer{
        List<Item>  items;
    }

    class Item{
        TrackData track;
    }
    
    class TrackData{
        String name;
        List<Artist> artists;
    }

    class Artist{
        String name;
    }
    public static void main(String[] args) throws Exception {
        spotifyAuth tokAuth = new spotifyAuth();
        tokAuth.setToken();
        String GeminiApiKey = "YOUR_GEMINI_API_KEY";
        System.out.println(tokAuth.getToken());
        String playlist_id = "4o67cwZejOI7KgjYUwAMGi";
        HttpRequest getRequest = HttpRequest.newBuilder()
              .uri(URI.create("https://api.spotify.com/v1/playlists/"+playlist_id))
              .header("Authorization", "Bearer " + tokAuth.getToken())
              .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        System.out.println(getResponse);
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(getResponse.body(), JsonObject.class);
        
        System.out.println(jsonResponse);

        PlaylistResponse playlist = gson.fromJson(jsonResponse, PlaylistResponse.class);

        List<String> tracksForGemini = new ArrayList<>();

        if(playlist.tracks != null && playlist.tracks.items != null){
            for(Item item: playlist.tracks.items){
                if(item.track!= null){
                    String songName = item.track.name;

                    String artistName = "Unknown";
                    if(item.track.artists != null && !item.track.artists.isEmpty()){
                        artistName = item.track.artists.get(0).name;
                    }

                    tracksForGemini.add(songName + "-" + artistName);
                }
            }
        }
        System.out.println(tracksForGemini);
    
    Client geminiClient = Client.builder().apiKey(GeminiApiKey).build();
   
    GenerateContentResponse geminiRoast =
        geminiClient.models.generateContent(
            "gemini-2.5-flash", "Act as a savage music critic. Ruthlessly roast the musical taste, personality, and vibe of the user based on this playlist:"+ tracksForGemini , null );
        
        System.out.println(geminiRoast.text());


    }
}
