package pas.com.mm.shoopingcart.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pas.com.mm.shoopingcart.DetailActivity;
import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.fragments.ContactFragment;

/**
 * Created by phyo on 01/10/2016.
 */
public abstract class CommonActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    public abstract int getViewId();
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
        if(id== android.R.id.home) {

            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (id == R.id.action_contact) {


            Intent intent = new Intent(getActivityContext(), DetailActivity.class);intent.putExtra("POSITION", id);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   public abstract Context getActivityContext();
}
