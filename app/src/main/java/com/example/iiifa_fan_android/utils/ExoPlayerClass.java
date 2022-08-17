package com.example.iiifa_fan_android.utils;//package com.example.mentalhealthpatient.utils;
//
//import android.content.Context;
//import android.net.Uri;
//
//import com.google.android.exoplayer2.DefaultLoadControl;
//import com.google.android.exoplayer2.DefaultRenderersFactory;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.LoadControl;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.TrackSelection;
//import com.google.android.exoplayer2.trackselection.TrackSelector;
//import com.google.android.exoplayer2.upstream.BandwidthMeter;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultAllocator;
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
//import com.example.mentalhealthpatient.R;
//
//public class ExoPlayerClass {
//
//    Context context;
//    SimpleExoPlayer player;
//
//    public ExoPlayerClass(Context context) {
//        this.context = context;
//    }
//
//    public SimpleExoPlayer initializePlayer() {
//        if (player == null) {
//            // 1. Create a default TrackSelector
//            LoadControl loadControl = new DefaultLoadControl(
//                    new DefaultAllocator(true, 16),
//                    VideoPlayerConfig.MIN_BUFFER_DURATION,
//                    VideoPlayerConfig.MAX_BUFFER_DURATION,
//                    VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
//                    VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);
//
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//            TrackSelection.Factory videoTrackSelectionFactory =
//                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
//            TrackSelector trackSelector =
//                    new DefaultTrackSelector(videoTrackSelectionFactory);
//            // 2. Create the player
//            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), trackSelector, loadControl);
//            return player;
//            //videoFullScreenPlayer.setPlayer(player);
//        } else {
//            return null;
//        }
//    }
//
//    public void buildMediaSource(Uri mUri) {
//        // Measures bandwidth during playback. Can be null if not required.
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        // Produces DataSource instances through which media data is loaded.
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
//                Util.getUserAgent(context, context.getString(R.string.app_name)), bandwidthMeter);
//        // This is the MediaSource representing the media to be played.
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(mUri);
//        // Prepare the player with the source.
//        player.prepare(videoSource);
//        player.setPlayWhenReady(true);
//        //player.addListener(context);
//    }
//
//    public void releasePlayer() {
//        if (player != null) {
//            player.release();
//            player = null;
//        }
//    }
//
//    public void pausePlayer() {
//        if (player != null) {
//            player.setPlayWhenReady(false);
//            player.getPlaybackState();
//        }
//    }
//
//    public void resumePlayer() {
//        if (player != null) {
//            player.setPlayWhenReady(true);
//            player.getPlaybackState();
//        }
//    }
//}
