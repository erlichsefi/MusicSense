package metaextract.nkm.com.myplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton btnForward, btnBackward, btnNext, btnPrevious, btnPlay, btnRepeat, btnShuffle;

    private ImageView songImageView;
    private TextView songTitleLabel, songCurrentDurationLabel, songTotalDurationLabel;

    private SeekBar songProgressBar;
    private MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();
    private Utilities utils;

    private int progress;
    private int songId = -1;
    private String lastSongName = "...", currentSongName = "", songTotalDuration = "";

    private boolean isShuffle = false, isRepeat = false;
    private SendToWear sendToWear;
    private ArrayList<Song> songsList = new ArrayList<>();

    private DataReceiveManager dataReceiveManagerAcc, dataReceiveManagerGravity,
            dataReceiveManagerPressure, dataReceiveManagerMagneticField, dataReceiveManagerOrientation,
            dataReceiveManagerRotationVector, dataReceiveManagerActivity, dataReceiveManagerSongList,
            dataReceiveManagerHeartRate, dataReceiveManagerStepCounter, dataReceiveManagerGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        btnForward = findViewById(R.id.btnForward);
        btnBackward = findViewById(R.id.btnBackward);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnShuffle = findViewById(R.id.btnShuffle);
        songImageView = findViewById(R.id.songImage);
        songTitleLabel = findViewById(R.id.songTitle);
        songProgressBar = findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = findViewById(R.id.songTotalDurationLabel);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        utils = new Utilities();
        songProgressBar.setOnSeekBarChangeListener(this);
        sendToWear = SendToWear.getInstance(this);
        MessageReceiveManager.getInstance(this);


        dataReceiveManagerGps = DataReceiveManager.getInstanceGps(this);
        dataReceiveManagerAcc = DataReceiveManager.getInstanceAcc(this);
        dataReceiveManagerHeartRate = DataReceiveManager.getInstanceHeartRate(this);
        dataReceiveManagerStepCounter = DataReceiveManager.getInstanceStepCounter(this);
        dataReceiveManagerGravity = DataReceiveManager.getInstanceGravity(this);
        dataReceiveManagerPressure = DataReceiveManager.getInstancePressure(this);
        dataReceiveManagerMagneticField = DataReceiveManager.getInstanceMagneticField(this);
        dataReceiveManagerOrientation = DataReceiveManager.getInstanceOrientation(this);
        dataReceiveManagerRotationVector = DataReceiveManager.getInstanceRotationVector(this);

        // Checking for permission
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        // If there is permission then we will get the song list
        if (permissionCheck == 0) {
            songsList = getPlayList();
            // Sort abc....
            Collections.sort(songsList, new Comparator<Song>() {
                public int compare(Song a, Song b) {
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
            if (songsList.size() > 0) {
                currentSongName = songsList.get(1).getTitle();
            }
            // Create SongList file
            dataReceiveManagerSongList = new DataReceiveManager(this, "SongList");
            dataReceiveManagerSongList.addSongList(songsList);
            // Create Activity file
            dataReceiveManagerActivity = new DataReceiveManager(this, "Activity");
            WriteToActivity("App start");
        }

        // Checks whether there was a request for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // If permission denied
            // No explanation needed, we can request the permission.
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an app-defined int constant.
            // The callback method gets the result of the request.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response!
                // After the user sees the explanation, try again to request the permission.
            }
            // Requesting permission
            else ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.BODY_SENSORS,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    /**
     * Getting approval for permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            // READ_EXTERNAL_STORAGE
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED //READ_EXTERNAL_STORAGE
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED //BODY_SENSORS
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED)//ACCESS_COARSE_LOCATION
                {
                    songsList = getPlayList();
                    //Sort abc....
                    Collections.sort(songsList, new Comparator<Song>() {
                        public int compare(Song a, Song b) {
                            return a.getTitle().compareTo(b.getTitle());
                        }
                    });

                    if (songsList.size() > 0) {
                        currentSongName = songsList.get(0).getTitle();
                    }
                    // Create SongList file
                    dataReceiveManagerSongList = new DataReceiveManager(this, "SongList");
                    dataReceiveManagerSongList.addSongList(songsList);
                    // Create Activity file
                    dataReceiveManagerActivity = new DataReceiveManager(this, "Activity");
                    WriteToActivity("App start");

                    // Permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    // Permission denied, boo! Disable the functionality that depends on this permission.
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            }
            // Other 'case' lines to check for other permissions this app might request
        }
    }

    /**
     * Creates array of the song list.
     *
     * @return - array representing the song list.
     */
    public ArrayList<Song> getPlayList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        @SuppressLint("Recycle") Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        if (musicCursor != null && musicCursor.moveToFirst()) {
            // Get columns
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DISPLAY_NAME);
            int columnIndex = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int DurationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            // Add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thiData = musicCursor.getString(columnIndex);
                String thiAlbum = musicCursor.getString(albumColumn);
                String DURATION = utils.milliSecondsToTimer(musicCursor.getLong(DurationColumn));

                byte[] PicSong;
                Bitmap songImage = null;
                String genre;
                mediaMetadataRetriever.setDataSource(thiData);
                genre = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                PicSong = mediaMetadataRetriever.getEmbeddedPicture();
                try {
                    songImage = BitmapFactory.decodeByteArray(PicSong, 0, PicSong.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.songsList.add(new Song(thisId, thisTitle, thisArtist, thiData, thiAlbum, genre, DURATION, songImage));
            }
            while (musicCursor.moveToNext());
        }
        return songsList;
    }

    /**
     * Function to play a song
     *
     * @param songIndex - index of song
     */
    public void playSong(int songIndex) {
        // Play song
        try {
            sendToWear.sendMessage("Act", "start");
            Uri uri = Uri.parse(songsList.get(songIndex).getdata());
            lastSongName = currentSongName;
            currentSongName = songsList.get(songIndex).getTitle();
            dataReceiveManagerAcc.setSongName(currentSongName + "-Acc");
            dataReceiveManagerGps.setSongName(currentSongName + "-Gps");
            dataReceiveManagerGravity.setSongName(currentSongName + "-Gravity");
            dataReceiveManagerPressure.setSongName(currentSongName + "-Pressure");
            dataReceiveManagerHeartRate.setSongName(currentSongName + "-HeartRate");
            dataReceiveManagerStepCounter.setSongName(currentSongName + "-StepCounter");
            dataReceiveManagerOrientation.setSongName(currentSongName + "-Orientation");
            dataReceiveManagerMagneticField.setSongName(currentSongName + "-MagneticField");
            dataReceiveManagerRotationVector.setSongName(currentSongName + "-RotationVector");
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();

            // Displaying Song title
            String songTitle = songsList.get(songIndex).getTitle();
            songTitleLabel.setText(songTitle);
            if (songsList.get(songIndex).getImage() == null) {
                songImageView.setImageResource(R.drawable.adele);
            } else {
                songImageView.setImageBitmap(songsList.get(songIndex).getImage());
            }

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.img_btn_pause);
            // Set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);
            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update timer on seek bar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration;
            long currentDuration;
            try {
                totalDuration = mediaPlayer.getDuration();
                currentDuration = mediaPlayer.getCurrentPosition();
            } catch (Exception e) {
                totalDuration = 0;
                currentDuration = 0;
            }
            // Displaying Total Duration time
            songTotalDuration = " " + utils.milliSecondsToTimer(totalDuration);
            songTotalDurationLabel.setText(songTotalDuration);
            // Displaying time completed playing
            String songCurrentDuration = " " + utils.milliSecondsToTimer(currentDuration);
            songCurrentDurationLabel.setText(songCurrentDuration);
            // Updating progress bar
            progress = (utils.getProgressPercentage(currentDuration, totalDuration));
            songProgressBar.setProgress(progress);
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
        dataReceiveManagerActivity.ActivityFILE(currentSongName, songId, songTotalDuration, lastSongName, progress + "% - " + seekBar.getProgress() + "%", "Progress bar");

        // Forward or backward to certain seconds.
        mediaPlayer.seekTo(currentPosition);

        // Update timer progress again.
        updateProgressBar();

    }

    /**
     * First time when app is start and clicked play or
     * In the song completeness, checks for isRepeat/isShuffle/next , and works accordingly
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (songId == -1) {
            playSong(0);
            songId++;
        } else if (isRepeat) {
            playSong(songId);
        } else if (isShuffle) {
            Random rand = new Random();
            songId = rand.nextInt(songsList.size() - 1);
            playSong(songId);
        } else if (songId < (songsList.size() - 1)) {
            playSong(songId + 1);
            songId++;
        } else {
            playSong(0);
            songId = 0;
        }
    }

    /**
     * Button Go to song list
     */
    public void ClickPlaylist(View view) {
        Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
        startActivityForResult(i, 100);
    }

    /**
     * Receiving song index from playlist view and play the song
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            songId = Objects.requireNonNull(data.getExtras()).getInt("songTitle");
            // play selected song
            playSong(songId);
            WriteToActivity("Choose song");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras().getInt("10") == 1) {
            btnPlay.performClick();
        }
        if (intent.getExtras().getInt("20") == 2) {
            btnBackward.performClick();
        }
        if (intent.getExtras().getInt("30") == 3) {
            btnPrevious.performClick();
        }
        if (intent.getExtras().getInt("40") == 4) {
            btnNext.performClick();
        }
        if (intent.getExtras().getInt("50") == 5) {
            btnForward.performClick();
        }
    }

    public void backward(View view) {
        // get current song position
        int currentPosition = mediaPlayer.getCurrentPosition();
        // check if seekBackward time is greater than 0 sec
        int seekBackwardTime = 5000;
        if (currentPosition - seekBackwardTime >= 0) {
            // forward song
            mediaPlayer.seekTo(currentPosition - seekBackwardTime);
        } else {
            // backward to starting position
            mediaPlayer.seekTo(0);
        }
        WriteToActivity("Backward");

    }

    public void Previous(View view) {
        if (songId > 0) {
            playSong(songId - 1);
            songId = songId - 1;
        } else {
            // play last song
            playSong(songsList.size() - 1);
            songId = songsList.size() - 1;
        }
        WriteToActivity("Previous");

    }

    public void play(View view) {
        // check for already playing
        if (mediaPlayer.isPlaying()) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                sendToWear.sendMessage("Act", "stop");
                WriteToActivity("Stop");
                // Changing button image to play button
                btnPlay.setImageResource(R.drawable.img_btn_play);
            }
        } else {
            // Resume song
            if (mediaPlayer != null) {
                mediaPlayer.start();
                sendToWear.sendMessage("Act", "start");
                WriteToActivity("Play");
                // Changing button image to pause button
                btnPlay.setImageResource(R.drawable.img_btn_pause);
            }
        }

    }

    public void Forward(View view) {
        // get current song position
        int currentPosition = mediaPlayer.getCurrentPosition();
        // check if seekForward time is lesser than song duration
        int seekForwardTime = 5000;
        if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
            // forward song
            mediaPlayer.seekTo(currentPosition + seekForwardTime);
        } else {
            // forward to end position
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }
        WriteToActivity("Forward");

    }

    public void Next(View view) {
        // Check if there id a next song.
        if (songId < (songsList.size() - 1)) {
            playSong(songId + 1);
            songId = songId + 1;
        } else {
            // Play first song
            playSong(0);
            songId = 0;
        }
        WriteToActivity("Next");
    }

    public void Repeat(View view) {
        if (isRepeat) {
            isRepeat = false;
            Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
            WriteToActivity("Repeat off");
            btnRepeat.setImageResource(R.drawable.img_btn_repeat);
        } else {
            // make repeat to true
            isRepeat = true;
            Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
            // make shuffle to false
            /////    isShuffle = false;
            WriteToActivity("Repeat on");
            btnRepeat.setImageResource(R.drawable.img_btn_repeat_pressed);
        }
    }

    /**
     * Shuffle
     */
    public void Shuffle(View view) {
        if (isShuffle) {
            isShuffle = false;
            Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
            WriteToActivity("Shuffle off");
            btnShuffle.setImageResource(R.drawable.img_btn_shuffle);
        } else {
            isShuffle = true;
            Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
            WriteToActivity("Shuffle on");
            btnShuffle.setImageResource(R.drawable.img_btn_shuffle_pressed);
        }
    }

    /**
     * Info button
     */
    public void DataInformation2(View view) {
        Intent i = new Intent(getApplicationContext(), DataShow.class);
        startActivityForResult(i, 100);
    }

    public void WriteToActivity(String activity) {
        dataReceiveManagerActivity.ActivityFILE(currentSongName, songId, songTotalDuration, lastSongName, progress + "%", activity);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        WriteToActivity("Destroy");
    }
}