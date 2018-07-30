package metaextract.nkm.com.myplayer;

import android.graphics.Bitmap;

public class Song {

    private long Id;
    private String Title;
    private String Artist;
    private String Data;
    private String Album;
    private String Genre;
    private String Duration;
    private Bitmap SongImage;

    public Song(long songID, String songTitle) {
        Id = songID;
        Title = songTitle;
    }

    public Song(long songID,
                String songTitle,
                String songArtist,
                String songData,
                String songAlbum,
                String songGenre, String songDuration, Bitmap songImage) {
        Id = songID;
        Title = songTitle;
        Artist = songArtist;
        Data = songData;
        Album = songAlbum;
        Genre = songGenre;
        Duration = songDuration;
        this.SongImage = songImage;
    }

    public long getID() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getArtist() {
        return Artist;
    }

    public String getdata() {
        return Data;
    }

    public String getDuration() {
        return Duration;
    }

    public String getAlbum() {
        return Album;
    }

    public String getGenre() {
        return Genre;
    }

    public Bitmap getImage() {
        return SongImage;
    }
}
