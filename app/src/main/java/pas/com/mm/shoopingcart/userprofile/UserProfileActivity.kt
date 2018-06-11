package pas.com.mm.shoopingcart.userprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import pas.com.mm.shoopingcart.R

import kotlinx.android.synthetic.main.activity_user_profile.*
import pas.com.mm.shoopingcart.logger.Log

class UserProfileActivity : AppCompatActivity() {

    var mUserName: TextView? =null
    var mEmailTextView: TextView? =null
    var mPhoneNumberTextView: TextView? =null


       public fun start(){
            val intent = Intent(this, UserProfileActivity::class.java)

            this.startActivity(intent)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
mUserName=findViewById(R.id.name_textview)
        mEmailTextView=findViewById(R.id.email_textview)
        mPhoneNumberTextView=findViewById(R.id.phone_textview)
        val user = FirebaseAuth.getInstance().currentUser
      var name=  user?.displayName

      var providerData:List<UserInfo> = user?.providerData!!
      mUserName?.setText(name)
        for(i in providerData){
            if(i.providerId=="facebook.com") {
                Log.i("USER++++", i.email)
                mEmailTextView?.text=i.email
                Log.i("USER++++", i.phoneNumber)
                mPhoneNumberTextView?.text=i.phoneNumber

            }

        }
        //var i:UserInfo=null
    }

}
