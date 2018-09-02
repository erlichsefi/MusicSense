package metaextract.nkm.com.myplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;

public class PlayListActivity extends ListActivity {

    private ArrayList<Song> songList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        new MediaMetadataRetriever();

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            // Get columns
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);

            // Add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                this.songList.add(new Song(thisId, thisTitle));
            }
            while (musicCursor.moveToNext());
        }
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        // ListView Adapter use
        ListView songView = getListView();
        SongAdapter songAdt = new SongAdapter(this, R.layout.playlist, songList);
        songView.setAdapter(songAdt);

        // Selecting single ListView item
        ListView lv = getListView();
        // Listening to single listItem click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                // Sending songIndex to PlayerActivity
                in.putExtra("songTitle", position);
                setResult(100, in);
                // Closing PlayListView
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent openMainActivity= new Intent(this, MainActivity.class);
        openMainActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        openMainActivity.putExtra("playList", 6);
        startActivity(openMainActivity);
    }
}