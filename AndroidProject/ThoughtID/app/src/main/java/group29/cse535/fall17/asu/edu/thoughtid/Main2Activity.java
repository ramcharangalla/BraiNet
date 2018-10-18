package group29.cse535.fall17.asu.edu.thoughtid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static group29.cse535.fall17.asu.edu.thoughtid.R.id.imageView;

public class Main2Activity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public static final String API_KEY = "AIzaSyDmAFyNXdrDnAUVHl7BMggtMv6YStItUPk";
    public static final String VIDEO_ID = "xrl_VxRXNo8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(API_KEY, this);
        ImageView loadingImage = (ImageView)findViewById(R.id.imageView);
        //URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464")
        //loadingImage.animate();
        //loadingImage.setBackgroundResource(R.drawable.giphy);
        Glide.with(Main2Activity.this)
                .load(R.drawable.load)
                .asGif()
                .into(loadingImage);
        //Intent serviceIntent = new Intent(Main2Activity.this, Webservice.class);
        //startService(serviceIntent);
        final String userid = getIntent().getStringExtra("userid");
        final String userid2=getIntent().getStringExtra("userid2");
        final String fogServerAddress=getIntent().getStringExtra("fogServerAddress");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(null!=userid2 && null!=fogServerAddress){
                    new AsyncTaskForWebService(Main2Activity.this).execute(userid,userid2,fogServerAddress,getIntent().getStringExtra("isAdaptive"));
                }
                else if(null!=userid2){
                    new AsyncTaskForWebService(Main2Activity.this).execute(userid,userid2,null,getIntent().getStringExtra("isAdaptive"));
                }
                else if(null!=fogServerAddress){
                    new AsyncTaskForWebService(Main2Activity.this).execute(userid,null,fogServerAddress,getIntent().getStringExtra("isAdaptive"));
                }
            }
        }, 8000);

        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Ez8P6N7fIqU&t=9s")));
        Log.i("Video", "Video Playing....");
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Back is disabled ",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        if(null== player) return;
        // Start buffering
        if (!b) {
            player.loadVideo(VIDEO_ID);
        };
        // Add listeners to YouTubePlayer instance
        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onAdStarted() { }

            @Override
            public void onError(YouTubePlayer.ErrorReason arg0) { }

            @Override
            public void onLoaded(String arg0) { }

            @Override
            public void onLoading() { }

            @Override
            public void onVideoEnded() { }

            @Override
            public void onVideoStarted() { }
        });
        //player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onBuffering(boolean arg0) { }

            @Override
            public void onPaused() { //Toast.makeText(Main2Activity.this,"Video Paused, Please play the video and relax",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onPlaying() {
                //Toast.makeText(Main2Activity.this,"Playing video",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSeekTo(int arg0) { }

            @Override
            public void onStopped() { }
        });


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
    }



}
