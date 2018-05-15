package pas.com.mm.shoopingcart.order

import pas.com.mm.shoopingcart.database.model.Model
import pas.com.mm.shoopingcart.database.model.OrderForm

interface BasketDataCallBack {

    abstract fun initDataLoaded(list: MutableList<OrderForm>)
    abstract fun onBasketRecyclerChange()
    abstract fun receiveResult(model: Model)
}