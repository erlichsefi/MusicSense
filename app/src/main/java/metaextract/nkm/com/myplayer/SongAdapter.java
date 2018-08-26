package metaextract.nkm.com.myplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<Song> {

    private ArrayList<Song> songList;

    SongAdapter(Context context, int resource, ArrayList<Song> songList) {
        super(context, resource, songList);
        this.songList = songList;
        LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.playlist_item, null);
        }
        Song positionSong = getItem(position);
        if (positionSong != null) {
            TextView textView = view.findViewById(R.id.songTitle);
            if (textView != null) {
                textView.setText(positionSong.getTitle());
            }
        }
        return view;
    }
}