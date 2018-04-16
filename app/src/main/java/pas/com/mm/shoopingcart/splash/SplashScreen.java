package pas.com.mm.shoopingcart.splash;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import pas.com.mm.shoopingcart.BuildConfig;
import pas.com.mm.shoopingcart.DetailActivity;
import pas.com.mm.shoopingcart.ItemGridView;
import pas.com.mm.shoopingcart.Main2Activity;
import pas.com.mm.shoopingcart.MainActivity;
import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.activities.OpenNotification;
import pas.com.mm.shoopingcart.common.ApplicationConfig;
import pas.com.mm.shoopingcart.database.DBListenerCallback;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.database.model.Model;
import pas.com.mm.shoopingcart.database.model.NotificationModel;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity implements DBListenerCallback {

   // AnimationDrawable rocketAnimation;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

       // ImageView rocketImage = (ImageView) findViewById(R.id.image_rotate);
      //  rocketImage.setBackgroundResource(R.drawable.splash_animation_list);
       // rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
       // rocketAnimation.start();

        TextView tv=(TextView)findViewById(R.id.txtConnection);
        tv.setText("Season Greeting");
        mVisible = true;
      //  mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        RotateAnimation anim = new RotateAnimation(0f, 350f, 50f, 50f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2000);
        ImageView flowerImg=(ImageView) findViewById(R.id.flower);
        //flowerImg.setAnimation(anim);
        flowerImg.startAnimation(anim);
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
         /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
       new Handler().postDelayed(new Runnable(){
         @Override
         public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                //Intent mainIntent = new Intent(SplashScreen.this,ItemGridView.class);
               // SplashScreen.this.startActivity(mainIntent);
               // SplashScreen.this.finish();
           }
        }, 3000);

      //  findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

       // ApplicationConfig config=new ApplicationConfig();
      //  config.init();

            DbSupport db = new DbSupport();
            db.loadItemList(this);

       // checkConnection();

        TextView textVersion=(TextView)findViewById(R.id.version);
        textVersion.setText( BuildConfig.VERSION_NAME);
    }


   private void checkConnection()
    {
        TextView tv=(TextView)findViewById(R.id.txtConnection);
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG);

            tv.setText("No Internet Connection");
        }
        else{
            tv.setText("Inter Connecition");
            Toast.makeText(this,"Yes Internet Connection",Toast.LENGTH_LONG);

        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void LoadCompleted(boolean b) {
        if(b){
            Intent mainIntent = new Intent(SplashScreen.this,ItemGridView.class);
            mainIntent.putExtra("TITLE", this.getIntent().getStringExtra("TITLE"));
            mainIntent.putExtra("BODY",this.getIntent().getStringExtra("BODY"));
            mainIntent.putExtra("CONTENT",this.getIntent().getStringExtra("CONTENT"));
            mainIntent.putExtra("TYPE",this.getIntent().getStringExtra("TYPE"));
            mainIntent.putExtra("MAIN_IMAGE",this.getIntent().getStringExtra("MAIN_IMAGE"));
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }
    }

    @Override
    public void receiveResult(Model model) {

    }
}
