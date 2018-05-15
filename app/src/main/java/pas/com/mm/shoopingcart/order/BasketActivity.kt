package pas.com.mm.shoopingcart.order

import android.os.Bundle
import android.os.Parcel
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_basket.*
import pas.com.mm.shoopingcart.R
import pas.com.mm.shoopingcart.database.model.Item
import pas.com.mm.shoopingcart.database.model.Model
import pas.com.mm.shoopingcart.database.model.OrderForm

class BasketActivity() : AppCompatActivity(), BasketDataCallBack {


    // private final var gridview: GridView? =null
    private val db = BasketDataPresenter()
    var basketList : List<OrderForm> = ArrayList()
    var gridview:RecyclerView? =null
    var adapter: BasketAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        setSupportActionBar(toolbar)

        gridview = findViewById<View>(R.id.recycle_basket) as RecyclerView


        val items = ArrayList<OrderForm>()
        adapter = BasketAdapter(this.baseContext, items)

        gridview?.adapter = adapter
        gridview?.layoutManager = LinearLayoutManager(this)
        val user = FirebaseAuth.getInstance().currentUser
        db.loadBasket(this, user?.uid);

    }


    override fun initDataLoaded(list: MutableList<OrderForm>) {

        basketList=list;
        for(i in 0..basketList.size-1){
            db.getItemById(basketList.get(i),this)
        }
    }

    override fun onBasketRecyclerChange() {
         adapter?.updateData(basketList)
    }

    override fun receiveResult(model: Model) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
