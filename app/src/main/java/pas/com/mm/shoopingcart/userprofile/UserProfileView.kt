package pas.com.mm.shoopingcart.userprofile

import pas.com.mm.shoopingcart.database.model.UserProfile

interface UserProfileView {
    fun successCreateUser()
    fun  setUserInfo(post: UserProfile?)
}