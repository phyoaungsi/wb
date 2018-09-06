package pas.com.mm.shoopingcart.order

import pas.com.mm.shoopingcart.database.model.Model
import pas.com.mm.shoopingcart.database.model.OrderForm
import pas.com.mm.shoopingcart.database.model.UserProfile

interface BasketDataCallBack {

    abstract fun initDataLoaded(list: MutableList<OrderForm>)
    abstract fun onBasketRecyclerChange()
    abstract fun receiveResult(model: Model)
    abstract fun successCheckout()
    abstract fun setUserInfo(post: UserProfile)
}