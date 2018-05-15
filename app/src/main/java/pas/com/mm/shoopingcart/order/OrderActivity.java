package pas.com.mm.shoopingcart.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.database.DbSupport;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button orderBtn=(Button) findViewById(R.id.btn_order);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbSupport db=new DbSupport();
                db.orderNewProduct("1",12.0,2);
            }
        });
    }

}
