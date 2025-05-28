package spotify_api;


public class Track {
    public String name;
    public String artist;
    public String album;

    public Track(String name, String artist, String album) {
        this.name = name;
        this.artist = artist;
        this.album = album;
    }
}