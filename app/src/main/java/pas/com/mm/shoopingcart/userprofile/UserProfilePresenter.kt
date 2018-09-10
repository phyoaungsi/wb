package pas.com.mm.shoopingcart.userprofile

import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.HttpsCallableResult
import pas.com.mm.shoopingcart.database.model.UserProfile
import pas.com.mm.shoppingcart.order.BasketDataPresenter
import java.util.HashMap
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import pas.com.mm.shoopingcart.util.FontUtil.setText
import android.R.attr.author
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener




class UserProfilePresenter{


    var  view: UserProfileView? = null;
    internal var mFunctions = FirebaseFunctions.getInstance()
    internal var mDatabase: DatabaseReference? = null
    var TAG="UserProfilePresenter"
    public fun retrieveUserProfile()
    {
       var user= FirebaseAuth.getInstance().currentUser!!.uid
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user);


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(UserProfile::class.java)
                var p:UserProfile=UserProfile()
                p.address= post?.address ?:""


                view!!.setUserInfo(post )
                // [START_EXCLUDE]
             //   mAuthorView.setText(post.author)
              //  mTitleView.setText(post.title)
             //   mBodyView.setText(post.body)
                // [END_EXCLUDE]
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // [START_EXCLUDE]
               // Toast.makeText(this@PostDetailActivity, "Failed to load post.",
                 //       Toast.LENGTH_SHORT).show()
                // [END_EXCLUDE]
            }
        }
        mDatabase!!.addValueEventListener(postListener)
    }
    public fun saveProfile(profile: UserProfile)
    {


        callSaveProfileApi(profile.address,profile.phone)
                .addOnCompleteListener(  {

                    if (!it.isSuccessful()) {
                        var e= it.exception

                        if (e is FirebaseFunctionsException) {
                            var ffe: FirebaseFunctionsException =  e as FirebaseFunctionsException
                            var  code: FirebaseFunctionsException.Code  = ffe.code
                            var details:Any? = ffe.details
                        }

                        // [START_EXCLUDE]
                       // Log.w(BasketDataPresenter.TAG, "addMessage:onFailure", e);

                        return@addOnCompleteListener;
                        // [END_EXCLUDE]
                    }

                    // [START_EXCLUDE]

                    var result:String = it.getResult();
                    view!!.successCreateUser()
                    //mMessageOutputField.setText(result);
                    // [END_EXCLUDE]

                });
        // [END call_add_message]
    }


    public fun callSaveProfileApi(address: String,phone:String): Task<String> {


            // Create the arguments to the callable function.
            val data = HashMap<Any,Any>()
            data.put("address", address)
            data.put("phone", phone)

            data.put("push", true)

            return mFunctions
                    .getHttpsCallable("createUser")
                    .call(data)
                    .continueWith(object : Continuation<HttpsCallableResult, String> {
                        @Throws(Exception::class)
                        override fun then(task: Task<HttpsCallableResult>): String {
                            // This continuation runs on either success or failure, but if the task
                            // has failed then getResult() will throw an Exception which will be
                            // propagated down.
                            return task.getResult().data.toString()
                        }
                    })

    }
    
}