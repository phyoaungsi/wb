package pas.com.mm.shoopingcart.order.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.view.ViewGroup
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.common_spinner_label.view.*
import pas.com.mm.shoopingcart.R
import java.util.*
import android.view.LayoutInflater
import com.firebase.ui.auth.data.model.Resource


class PaymentTypeAdapter(context: Context?, resource: Int, objects: MutableList<NameValue>?) : ArrayAdapter<NameValue>(context, resource, objects) {

    var mContext: Context? = context
    var mItems: MutableList<NameValue>? = objects
    var mResource: Int = resource
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, @Nullable convertView: View?, parent: ViewGroup): View {
        var view: View = mInflater.inflate(mResource, parent, false)
        var editText = view?.findViewById<TextView>(R.id.txt_label)
        editText?.text = mItems?.get(position)?.name
        return view
    }


}