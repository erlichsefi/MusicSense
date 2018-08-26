package metaextract.nkm.com.myplayer;

import android.graphics.Bitmap;

public class Song {

    private long id;
    private String title, artist, data, album, genre, duration;
    private Bitmap image;

    Song(long id, String title) {
        this.id = id;
        this.title = title;
    }

    Song(long id, String title, String artist, String data, String album, String genre, String duration, Bitmap image) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.data = data;
        this.album = album;
        this.genre = genre;
        this.duration = duration;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getData() {
        return data;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public Bitmap getImage() {
        return image;
    }
}