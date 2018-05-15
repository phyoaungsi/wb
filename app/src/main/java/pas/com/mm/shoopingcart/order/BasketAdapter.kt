package pas.com.mm.shoopingcart.order

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import pas.com.mm.shoopingcart.R
import pas.com.mm.shoopingcart.database.model.OrderForm
import pas.com.mm.shoopingcart.image.RoundedCornersTransform

class BasketAdapter constructor(context_: Context,list_:List<OrderForm> ): RecyclerView.Adapter<BasketAdapter.ViewHolder>() {


    var list = list_
    var context:Context=context_

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

        holder?.title?.text=list.get(position).item.title
        holder?.desc?.text=list.get(position).item.description
        holder?.quantity?.text=list.get(position).quantity.toString()

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


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val title:TextView? = itemView?.findViewById(R.id.item_title_label)
        val desc:TextView? = itemView?.findViewById<TextView>(R.id.item_description_label)
        val quantity:TextView? = itemView?.findViewById<TextView>(R.id.item_quantity_label)
        val image:ImageView?=itemView?.findViewById<ImageView>(R.id.item_stock_image)
    }

}