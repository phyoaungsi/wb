package pas.c

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pas.com.mm.shoopingcart.common.ApplicationConfig
import pas.com.mm.shoopingcart.database.DBListenerCallback
import pas.com.mm.shoopingcart.database.model.BasketModel
import pas.com.mm.shoopingcart.database.model.Config
import pas.com.mm.shoopingcart.database.model.Item
import pas.com.mm.shoopingcart.database.model.OrderForm
import pas.com.mm.shoopingcart.order.BasketDataCallBack
import java.util.*


class BasketDataPresenter {
    internal var database = FirebaseDatabase.getInstance()
    var result: Item? = null
    var item: Item=Item()
    var resultList: List<*>? = null
    fun writeMessage() {

        val myRef = database.getReference("message/items/")
        myRef.setValue("Hello, World!")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: " + value!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }


    fun writeNewPost(userId: String, username: String, title: String, body: Double) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val myRef = database.getReference("message/items/")
        val key = myRef.child("posts").push().key
        val post = Item(userId, username, title, body)
        val postValues = post.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/items/$key"] = postValues
        childUpdates["/user-items/$userId/$key"] = postValues

        myRef.updateChildren(childUpdates)
    }

    fun listenDataChange() {
        val myRef = database.getReference("message/items/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                for (postSnapshot in dataSnapshot.children) {
                    // TODO: handle the post
                    val post = postSnapshot.getValue(Item::class.java)
                    // [START_EXCLUDE]
                    Log.d(TAG, "Value is: " + post!!.code)
                    Log.d(TAG, "Value is: " + post.description)
                    Log.d(TAG, "Value is: " + post.amount!!)
                }


                // [END_EXCLUDE]
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // [START_EXCLUDE]
                // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                //        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }
        myRef.addValueEventListener(postListener)

    }

    fun loadBasket(cb: BasketDataCallBack?, userId: String?) {
        try {
            database.setPersistenceEnabled(true)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        var list: MutableList<OrderForm> = ArrayList()
        val cartDataListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val genericTypeIndicator = object : GenericTypeIndicator<HashMap<String, OrderForm>>() { }
                val posts = dataSnapshot.getValue(genericTypeIndicator)
                list = ArrayList()
                if (posts != null) {
                    for (i in posts.keys) {
                      var order: OrderForm? =  posts.get(i)
                    // var order?: OrderForm=
                      //  Log.d("DBSupport", i.getDescription())
                        order?.key  =i

                        list.add(order!!)
                    }
                }
                cb?.initDataLoaded(list)
                Log.d("DBSupport", "---------LOADED--------")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // [START_EXCLUDE]
                // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                //        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }
        try {
            val myRef = database.getReference("orders/"+userId+"/")
            myRef.addValueEventListener(cartDataListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun removeCartItem(userId: String?,productId:String?) {
        try {
            database.setPersistenceEnabled(true)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val myRef = database.getReference("orders/"+userId+"/"+productId+"/")
            myRef.setValue(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getItemById(order: OrderForm,callback:BasketDataCallBack): Item {
        val myRef = database.getReference("message/items/" + order.productId.trim { it <= ' ' })


        val r = Item()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                //   for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                // TODO: handle the post

                val dbResult = dataSnapshot.getValue(Item::class.java)
              if(dbResult!=null) {
                  r.amount = dbResult!!.amount
                  r.setCode(dbResult.getCode())
                  r.setDescription(dbResult.getDescription())
                  r.setDiscount(dbResult.getDiscount()!!)
                  r.setHtmlDetail(dbResult.getHtmlDetail())
                  r.setImgUrl(dbResult.getImgUrl())
                  r.setTitle(dbResult.getTitle())
                  r.setKey(dataSnapshot.key)
                  val list = dbResult.getChildren()
                  r.setChildren(list)
                  result = r
                  order.item = result
                  callback.onBasketRecyclerChange()
              }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())

            }
        }

        myRef.addValueEventListener(postListener)
        return r
    }


    fun getItemById(id: String, cb: BasketDataCallBack): Item {
        val myRef = database.getReference("message/items/" + id.trim { it <= ' ' })


        val r = Item()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                //   for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                // TODO: handle the post

                val dbResult = dataSnapshot.getValue(Item::class.java)
                r.amount = dbResult!!.amount
                r.setCode(dbResult.getCode())
                r.setDescription(dbResult.getDescription())
                r.setDiscount(dbResult.getDiscount()!!)
                r.setHtmlDetail(dbResult.getHtmlDetail())
                r.setImgUrl(dbResult.getImgUrl())
                r.setTitle(dbResult.getTitle())
                r.setKey(dataSnapshot.key)
                val list = dbResult.getChildren()
                r.setChildren(list)
                result = r

                cb.receiveResult(r)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // [START_EXCLUDE]
                // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                //        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }

        myRef.addValueEventListener(postListener)
        return r
    }


    fun getItemsByType(type: String, cb: DBListenerCallback?) {
        val r = Item()
        val postListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val list2 = ArrayList<Item>()
                for (snapshot in dataSnapshot.children) {
                    val i = snapshot.getValue(Item::class.java)
                    i!!.setKey(snapshot.key)
                    list2.add(i)
                }
                Collections.sort(list2)

                // [START_EXCLUDE]
                //list.add(post);
                //   }


                // [END_EXCLUDE]
                //database.goOffline();
                if (cb != null) {
                    resultList = list2
                    cb.LoadCompleted(true)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // [START_EXCLUDE]
                // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                //        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }

        try {
            val myRef = FirebaseDatabase.getInstance().getReference("message/items/")
            // myRef.orderByChild("type").equalTo(type)

            myRef.orderByChild("type").equalTo(type).addListenerForSingleValueEvent(postListener1)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getConfig(cb: DBListenerCallback): Config {
        val myRef = database.getReference("config")


        val r = Config()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                //   for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                // TODO: handle the post

                val dbResult = dataSnapshot.getValue(Config::class.java)
                r.setMessageNumber(dbResult!!.getMessageNumber())
                r.setPromotionOn(dbResult.getPromotionOn())
                r.setPhoneNumber(dbResult.getPhoneNumber())
                r.setOpen(dbResult.getOpen())
                r.setPromotionBanner(dbResult.getPromotionBanner())
                r.setPromotionImage(dbResult.getPromotionImage())
                r.setSmsMessage(dbResult.getSmsMessage())
                r.setAddress1(dbResult.getAddress1())
                r.setAddress2(dbResult.getAddress2())
                ApplicationConfig.smsMessage = dbResult.getSmsMessage()
                ApplicationConfig.open = r.getOpen()
                ApplicationConfig.smsNumber = r.getMessageNumber()
                ApplicationConfig.phoneNumber = r.getPhoneNumber()
                ApplicationConfig.address1 = r.getAddress1()
                ApplicationConfig.address2 = r.getAddress2()

                // callback.LoadCompleted(true);
                cb.receiveResult(r)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // [START_EXCLUDE]
                // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                //        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }

        myRef.addValueEventListener(postListener)
        return r
    }

    fun orderNewProduct(productId: String, amount: Double, quantity: Int) {
        val mDatabase = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        val currentLoginMember = user!!.uid
        val order = OrderForm(productId, amount, quantity, currentLoginMember)
        val key = mDatabase.child("orders").child(currentLoginMember).push().key
        mDatabase.child("orders").child(currentLoginMember).child(key).setValue(order)
    }

    companion object {
        private val TAG = "DB"
        var list: MutableList<Item> = ArrayList()
        var favList: List<Item> = ArrayList()



    }
}
