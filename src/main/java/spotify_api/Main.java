package spotify_api;

import java.util.*;
import java.util.regex.*;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        try {
        if (args.length < 1) {
            logger.info("Usage: java Main <spotify_playlist_url>");
            return;
        }

        String url = args[0];
        String playlistId = extractPlaylistId(url);
        if (playlistId == null) {
            logger.info("Invalid Spotify playlist URL.");
            return;
        }

        SpotifyClient client = new SpotifyClient();
        String token = client.getAccessToken();
        List<Track> tracks = client.getTracks(playlistId, token);

        ExcelExporter exporter = new ExcelExporter();
        exporter.export(tracks, "playlist.xlsx");
    } catch (Exception e) {
        logger.severe("Error occurred: " + e.getMessage());
    }
}

    private static String extractPlaylistId(String url) {
        Pattern pattern = Pattern.compile("playlist/([a-zA-Z0-9]+)");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }
}