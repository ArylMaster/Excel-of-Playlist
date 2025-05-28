package spotify_api;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import java.util.logging.Logger;


public class SpotifyClient {
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final Logger logger = Logger.getLogger(SpotifyClient.class.getName());

    public String getAccessToken() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String body = "grant_type=client_credentials";
        String auth = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
        logger.info("Encoded auth: " + auth);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://accounts.spotify.com/api/token"))
            .header("Authorization", "Basic " + auth)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Response: " + response.body());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.body()).get("access_token").asText();
    }

        public List<Track> getTracks(String playlistId, String accessToken) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.spotify.com/v1/playlists/" + playlistId + "/tracks"))
            .header("Authorization", "Bearer " + accessToken)
            .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("HTTP Status: " + response.statusCode());
        logger.info("Response: " + response.body());
    
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch tracks. HTTP Status: " + response.statusCode());
        }
    
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
    
        List<Track> tracks = new ArrayList<>();
        for (JsonNode item : root.get("items")) {
            JsonNode track = item.get("track");
            String name = track.get("name").asText();
            String artist = track.get("artists").get(0).get("name").asText();
            String album = track.get("album").get("name").asText();
            tracks.add(new Track(name, artist, album));
        }
        return tracks;
    }
}
