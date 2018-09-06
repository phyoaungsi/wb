package pas.com.mm.shoopingcart.userprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import pas.com.mm.shoopingcart.R

import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.content_user_profile.*
import pas.com.mm.shoopingcart.database.model.UserProfile
import pas.com.mm.shoopingcart.logger.Log

class UserProfileActivity : AppCompatActivity(),UserProfileView {



    var mUserName: TextView? =null
    var mEmailTextView: TextView? =null
    var mPhoneNumberTextView: TextView? =null
    var presenter:UserProfilePresenter?=null

       public fun start(){
            val intent = Intent(this, UserProfileActivity::class.java)

            this.startActivity(intent)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)
        presenter=UserProfilePresenter()
        presenter!!.view=this

        mUserName=findViewById(R.id.name_textview)


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

        email_editText.setText(user.email)
        user.phoneNumber
        //var i:UserInfo=null
        profile_save.setOnClickListener { onClick() }
        presenter!!.retrieveUserProfile()
    }

    fun onClick()
    {
          var email=  email_editText.text.toString()
          var phone=phone_editText.text.toString()
          var address=address_text.text.toString()
        var profile: UserProfile =UserProfile()
      //  profile.email=email
        profile.phone=phone
        profile.address=address

        presenter!!.saveProfile(profile)


    }


    override fun successCreateUser() {
       Toast.makeText(this,R.string.user_profile_save,Toast.LENGTH_SHORT).show()
    }


    override fun setUserInfo(profile: UserProfile?) {
        address_text.setText(profile?.address)
        phone_editText.setText(profile?.phone)
    }

}
