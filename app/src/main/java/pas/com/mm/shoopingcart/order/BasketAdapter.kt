package pas.com.mm.shoopingcart.order

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import pas.c.BasketDataPresenter
import pas.com.mm.shoopingcart.R
import pas.com.mm.shoopingcart.database.model.OrderForm
import pas.com.mm.shoopingcart.image.RoundedCornersTransform

class BasketAdapter constructor(context_: Context,list_:List<OrderForm> ): RecyclerView.Adapter<BasketAdapter.ViewHolder>() {


    var list = list_
    var context:Context=context_
    var basketPresenter:BasketDataPresenter= BasketDataPresenter()
    override fun getItemId(p0: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_recycler_item, parent, false))
    }

    override fun getItemCount(): Int {
       return  list.count()
    }

     fun updateData(list_:List<OrderForm>) {
        list=list_
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: BasketAdapter.ViewHolder?, position: Int) {

        if(list.get(position).item!=null) {
            holder?.title?.text = list.get(position).item.title
            holder?.desc?.text = list.get(position).item.description
            holder?.quantity?.text = list.get(position).quantity.toString()

            holder?.mDecreaseButton?.setOnClickListener(View.OnClickListener {

               var txtQty:String= holder?.quantity?.text.toString()
               var count=txtQty.toInt()
                if(count!=0) count--
                holder?.quantity?.text=count.toString()
            })

            holder?.mIncreaseButton?.setOnClickListener(View.OnClickListener {

                var txtQty:String= holder?.quantity?.text.toString()
                var count=txtQty.toInt()
                count++
                holder?.quantity?.text=count.toString()
            })

          holder?.mDeleteButton?.setOnClickListener(View.OnClickListener {


             var builder:AlertDialog.Builder  =  AlertDialog.Builder(context)
              builder.setMessage("Are you sure you want to remove the item?")
                      .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { d: DialogInterface,i:Int->
                         // list.drop(position)
                         // notifyItemRemoved(position)
                          val user = FirebaseAuth.getInstance().currentUser
                          if (user != null) {
                              basketPresenter.removeCartItem(user.uid,list[position].key)
                          }

                      })
                      .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { d: DialogInterface,i:Int->
                         d.dismiss()
                      })
              // Create the AlertDialog object and return it
              builder.create().show()


        })
            val mPicasso = Picasso.with(context)
            // mPicasso.setIndicatorsEnabled(true);
            // mPicasso.with(this.getmContext())
            //  mPicasso.setLoggingEnabled(true);

            mPicasso.load(list.get(position).item.imgUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_menu_manage)
                    //   .networkPolicy(NetworkPolicy.OFFLINE)

                    .resize(400, 400)
                    .centerCrop().transform(RoundedCornersTransform())
                    .into(holder?.image)
        }
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val title:TextView? = itemView?.findViewById(R.id.item_title_label)
        val desc:TextView? = itemView?.findViewById<TextView>(R.id.item_description_label)
        val quantity:TextView? = itemView?.findViewById<TextView>(R.id.item_quantity_label)
        val image:ImageView?=itemView?.findViewById<ImageView>(R.id.item_stock_image)
        var mDeleteButton:ImageButton?=itemView?.findViewById<ImageButton>(R.id.delete_button)
        var mIncreaseButton:Button?=itemView?.findViewById<Button>(R.id.increase_button)
        var mDecreaseButton:Button?=itemView?.findViewById<Button>(R.id.decrease_button)
    }

}