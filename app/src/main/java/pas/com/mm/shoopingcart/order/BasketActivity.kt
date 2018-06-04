package pas.com.mm.shoopingcart.order

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_basket.*
import pas.c.BasketDataPresenter
import pas.com.mm.shoopingcart.R
import pas.com.mm.shoopingcart.database.model.Model
import pas.com.mm.shoopingcart.database.model.OrderForm
import pas.com.mm.shoopingcart.order.adapter.NameValue
import pas.com.mm.shoopingcart.order.adapter.PaymentTypeAdapter

class BasketActivity() : AppCompatActivity(), BasketDataCallBack {


    // private final var gridview: GridView? =null
    private val db = BasketDataPresenter()
    private var mQuantityText:TextView? = null
    private var mSubtotalText:TextView? = null

    var basketList : List<OrderForm> = ArrayList()
    var gridview:RecyclerView? =null
    var adapter: BasketAdapter?=null
    var mDeliveryTypeSpinner:Spinner?=null
    var mPaymentTypeAdapter:ArrayAdapter<String>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        setSupportActionBar(toolbar)

        gridview = findViewById<View>(R.id.recycle_basket) as RecyclerView
        mSubtotalText=findViewById<TextView>(R.id.subtotal_amount_label)
        mQuantityText=findViewById<TextView>(R.id.quantity_amount_label)
        mDeliveryTypeSpinner=findViewById<Spinner>(R.id.delivery_type_spinner)
        val items = ArrayList<OrderForm>()
        adapter = BasketAdapter(this, items)

        gridview?.adapter = adapter
        var mLayout:LinearLayoutManager=LinearLayoutManager(this)
       mLayout.isSmoothScrollbarEnabled=false
        gridview?.layoutManager = mLayout
        gridview?.isNestedScrollingEnabled=false
        val user = FirebaseAuth.getInstance().currentUser

        var ddlList = mutableListOf<NameValue>()
        var value:NameValue= NameValue()
        value.name="Transfer"
        value.value="TXN"
        var value1:NameValue=NameValue()
        value1.name="Cash On Delivery"
        value1.value="COD"
        ddlList.add(value)
        ddlList.add(value1)

        val paymentTypes = arrayOf("Money Transfer", "Cash On Delivery")
        mPaymentTypeAdapter= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,paymentTypes)
        mDeliveryTypeSpinner?.adapter=mPaymentTypeAdapter

        db.loadBasket(this, user?.uid);

    }


    override fun initDataLoaded(list: MutableList<OrderForm>) {

        basketList=list;
        for(i in 0 until basketList.size){
            db.getItemById(basketList[i],this)
        }
    }

    override fun onBasketRecyclerChange() {
         adapter?.updateData(basketList)
         var subtotal: Double =0.0
        var qty: Int =0
        for(order in basketList){
            subtotal += order.amount
            qty += order.quantity
        }
        mSubtotalText?.text=subtotal.toString()
        mQuantityText!!.text=qty.toString()
    }

    override fun receiveResult(model: Model) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
