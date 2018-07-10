package com.example.android.bakingtime.Steps;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingtime.Description.DetailedList;
import com.example.android.bakingtime.Description.StepFragmentContent;
import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;



public class FullDescriptionFragment extends Fragment{
    @BindView(R.id.complete_short_desc) TextView shortDesc;
    @BindView(R.id.complete_desc) TextView completeDesc;
    @BindView(R.id.all_contents) RelativeLayout allContents;
    @BindView(R.id.player_view) SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.frameLayout) FrameLayout bottomNavigation;
    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.thumbnailImageView) ImageView ThumbnailViewImage;

    private Unbinder unbinder;
    private static final String SELECTION_MADE_ON_STATE = "SelectOn";
    private static final String VIDEO_AVAIL = "videoAvailablility";
    private static final String PLAY_STATE_RESTORE="playstate";

    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    public static boolean shouldAutoPlay=true;
    private BandwidthMeter bandwidthMeter;
    public static long playbackPosition=0;
    public static final String PLAYBACK_POSITION = "playback_position";

    public FullDescriptionFragment() {
    }

    public static int tempSelection=StepFragmentContent.currentSelection;
    public static int tempoFlag =StepFragmentContent.ultimateFlag;
    private static boolean videoAvailableFlag;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_left:
                    if(tempSelection>0) {
                        shouldAutoPlay = true;
                        playbackPosition=0;
                        tempSelection -= 1;
                        if(!(MainActivity.videoURL[DetailedList.id][tempSelection+1].equals(""))){
                            releasePlayer();
                        }
                        updatelist();
                    }else{
                        Toast.makeText(getContext(),"No Previous Contents",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case R.id.navigation_right:
                    if(tempSelection<tempoFlag-1) {
                        shouldAutoPlay = true;
                        playbackPosition=0;
                        tempSelection+=1;
                        if(!(MainActivity.videoURL[DetailedList.id][tempSelection-1].equals(""))){
                            releasePlayer();
                        }
                        updatelist();
                    }else{
                        Toast.makeText(getContext(),"Can't navigate furthur",Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmet_full_desc, container, false);
        unbinder= ButterKnife.bind(this,rootView);
        if(!MainActivity.tabletSize) {
            if (savedInstanceState != null) {
                shouldAutoPlay = savedInstanceState.getBoolean(PLAY_STATE_RESTORE);
                tempSelection = savedInstanceState.getInt(SELECTION_MADE_ON_STATE);
                videoAvailableFlag = savedInstanceState.getBoolean(VIDEO_AVAIL);
                playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            }
        }
        shortDesc.setText(MainActivity.shortDescription[DetailedList.id][tempSelection]);
        completeDesc.setText(MainActivity.description[DetailedList.id][tempSelection]);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        return rootView;
    }

    private void updatelist(){
        if(MainActivity.videoURL[DetailedList.id][tempSelection].equals("")){
            if(!MainActivity.thumbnailURL[DetailedList.id][tempSelection].equals("")){
                ThumbnailViewImage.setVisibility(View.VISIBLE);
                Picasso.with(getContext())
                        .load(MainActivity.thumbnailURL[DetailedList.id][tempSelection])
                        .into(ThumbnailViewImage);
            }
            simpleExoPlayerView.setVisibility(View.GONE);
            videoAvailableFlag=false;
        }else{
            ThumbnailViewImage.setVisibility(View.GONE);
            videoAvailableFlag=true;
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            bandwidthMeter = new DefaultBandwidthMeter();
            mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
            window = new Timeline.Window();
            if(player==null) {
                initializePlayer();
            }
        }
        if(MainActivity.tabletSize){
            bottomNavigation.setVisibility(View.GONE);
        }else{
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                allContents.setVisibility(View.VISIBLE);
                bottomNavigation.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = 700;
                simpleExoPlayerView.setLayoutParams(params);
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && videoAvailableFlag) {
                allContents.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                simpleExoPlayerView.setLayoutParams(params);
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                bottomNavigation.setVisibility(View.GONE);
            }
        }
    }

    private void initializePlayer() {
        simpleExoPlayerView.requestFocus();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        simpleExoPlayerView.setPlayer(player);
        player.setPlayWhenReady(shouldAutoPlay);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(MainActivity.videoURL[DetailedList.id][tempSelection]),
                mediaDataSourceFactory, extractorsFactory, null, null);
        player.seekTo(playbackPosition);
        player.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
            simpleExoPlayerView.destroyDrawingCache();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            Log.e("STATUS:",tempSelection+" @onStop");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        updatelist();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(videoAvailableFlag) {
            playbackPosition = player.getCurrentPosition();
        }else{
            playbackPosition=0;
        }
        if(player!=null) {
            shouldAutoPlay = player.getPlayWhenReady();
        }
        outState.putBoolean(PLAY_STATE_RESTORE,shouldAutoPlay);
        outState.putLong(PLAYBACK_POSITION, playbackPosition);
        outState.putInt(SELECTION_MADE_ON_STATE,tempSelection);
        outState.putBoolean(VIDEO_AVAIL,videoAvailableFlag);
    }
}
