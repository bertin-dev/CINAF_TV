package com.cinaf.android_tv.ui.player;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.cinaf.android_tv.R;
import com.cinaf.android_tv.translate.LocaleHelper;

import java.util.Locale;

public class Play extends Activity {

    private String videoUrl = "";

    private ProgressDialog pd;

    VideoView videoView;

    private Boolean isLive = false;
    private String videoType;
    private String videoTitle;
    private String videoImage;
    private String videoSubTile;
    private int vodeoId ;
    private String videoKind;

    String currentLanguage = (Locale.getDefault().getLanguage().contentEquals("fr")) ? "fr" : "en", currentLang;

    @Override
    protected void onStart() {
        super.onStart();
        //langue par défaut
        currentLanguage = getIntent().getStringExtra(currentLang);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        videoUrl = intent.getStringExtra("url");
        Log.w("TAG", "onCreate: " + videoUrl );

        videoView = findViewById(R.id.videoView);
        pd = new ProgressDialog(this);
        pd.setMessage("Buffering...");
        pd.setCancelable(true);
        
        playVideo();
    }

    private void playVideo() {

        try {

            //getWindow().setFormat(PixelFormat.TRANSLUCENT);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);

            //parse urk as URI
            Uri videoUri = Uri.parse(videoUrl);

            //set media controller to video view
            videoView.setMediaController(mediaController);

            //set video uri
            videoView.setVideoURI(videoUri);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    pd.dismiss();
                    videoView.start();
                }
            });

        } catch (Exception e){
            pd.dismiss();
            Toast.makeText(this, "111111111" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    /**
     * attachBaseContext(Context newBase) methode callback permet de verifier la langue au demarrage
     * @param newBase
     * @since 2021
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}