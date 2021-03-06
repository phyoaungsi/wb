package pas.com.mm.shoopingcart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import pas.com.mm.shoopingcart.activities.ContactActivity;
import pas.com.mm.shoopingcart.activities.OpenNotification;
import pas.com.mm.shoopingcart.activities.saveitem.SaveItemActivity;
//import pas.com.mm.shoopingcart.common.ApplicationConfig;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.NotificationModel;
import pas.com.mm.shoopingcart.fragments.itemgrid.ConfigDbListener;
import pas.com.mm.shoopingcart.order.BasketActivity;
import pas.com.mm.shoopingcart.splash.NotiItemDbListener;
import pas.com.mm.shoopingcart.splash.NotiPromoDbListener;
import pas.com.mm.shoopingcart.splash.SplashScreen;
import pas.com.mm.shoopingcart.userprofile.UserProfileActivity;
import pas.com.mm.shoopingcart.util.FontUtil;
import android.support.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class ItemGridView extends AppCompatActivity implements ImageGridFragment.OnFragmentInteractionListener,DetailFragment.OnFragmentInteractionListener {
    private static final String TAG = "ImageGridActivity";
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    ImageView mImageView;
    Context mContext;
    private static final String IS_PROMOTION_CONFIG_KEY = "is_promotion_on";
    private String isPromotionOn="invalid";


    public  static void startActivity(Context context){
        Intent mainIntent = new Intent(context,ItemGridView.class);
        context.startActivity(mainIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        mContext=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FontUtil.setText(this.getBaseContext(),toolbar,false);
/*
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                       // String msg = getString(R.string.save, token);
                        Log.i("ItemGridVIEW","onToken----"+ token);
                        Toast.makeText(ItemGridView.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
*/
      mImageView=this.findViewById(R.id.profile_image);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
     //  initRemoteConfig();
        Picasso.with(this)
                .load(user.getPhotoUrl())
                //  .networkPolicy(NetworkPolicy.)
                .placeholder(R.drawable.teenage_boy)
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        mImageView.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        mImageView.setImageResource(R.drawable.fav_icon_title);
                    }
                });

        mImageView.setOnClickListener((View v) ->{

            Intent intent = new Intent(this, UserProfileActivity.class);

            startActivity(intent);


        });
                // .resize(850, 850)
                //  .centerCrop()

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);


       // gridview.setAdapter(new ImageAdapter(this));
        DbSupport db=new DbSupport();
        ConfigDbListener configListener=new ConfigDbListener();
        View v=this.findViewById(R.id.banner);
        configListener.setView(v);
        configListener.setmContext(this);
        db.getConfig(configListener);


        NotificationModel noti=new NotificationModel();


        noti.setTitle( this.getIntent().getStringExtra("TITLE"));
        noti.setMessage(this.getIntent().getStringExtra("BODY"));
        noti.setContent(this.getIntent().getStringExtra("CONTENT"));
        noti.setType(this.getIntent().getStringExtra("TYPE"));
        noti.setMainImage(this.getIntent().getStringExtra("MAIN_IMAGE"));
        boolean notiPressed=false;
        if(this.getIntent().getStringExtra("TYPE")!=null ){
            if (noti.getType().equals("ITEM")) {


                NotiItemDbListener listener = new NotiItemDbListener(this);
                DbSupport db1 = new DbSupport();
                db1.getItemById(noti.getContent(), listener);
                notiPressed=true;

            }
            else if (noti.getType().equals("PROMO")) {
                DbSupport db2 = new DbSupport();
                NotiPromoDbListener listener = new NotiPromoDbListener(this);
                db2.getConfig(listener);
                notiPressed=true;
            }
        }
        isOnline();
    }

    public void onResume()
    {
        super.onResume();
        Log.i("ItemGridVIEW","RESUME ]]]]]]]]]]]]]]]]]----");
    }
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_contact) {

            Intent intent = new Intent(this, ContactActivity.class);intent.putExtra("POSITION", id);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_basket) {

            Intent intent = new Intent(this, BasketActivity.class);intent.putExtra("POSITION", id);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_favorite) {

            Intent intent = new Intent(this, SaveItemActivity.class);intent.putExtra("POSITION", id);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ImageGridFragment();
            Bundle args=new Bundle();
            args.putInt("PANEL",i);
            fragment.setArguments(args);

            // Our object is just an integer :-P
           // args.putInt(ImageGridFragment.ARG_OBJECT, i + 1);
           // fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return getResources().getString(R.string.title_tab1);
                case 0:
                    return getResources().getString(R.string.title_tab2);
               default:
                    return getResources().getString(R.string.title_tab3);

            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
