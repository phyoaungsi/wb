package pas.com.mm.shoopingcart.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import android.graphics.Bitmap
import pas.com.mm.shoopingcart.R.id.imageView
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


class CloudStorageUtil {


   public fun uploadFile(path:String)
    {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        // Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child("mountain")

        // Create a reference to 'images/mountains.jpg'
        val userId=FirebaseAuth.getInstance().currentUser?.uid
        val mountainImagesRef = storageRef.child("users/+"+userId+"/mountains.jpg")

        // While the file names are the same, the references point to different files
        mountainsRef.getName() == mountainImagesRef.getName()    // true
        mountainsRef.getPath() == mountainImagesRef.getPath()    // false
        val imageView:ImageView?= null
      //  val bitmap = (imageView?.getDrawable() as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
       // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val stream = FileInputStream(File(path))
        var uploadTask = mountainImagesRef.putStream(stream)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("TAG",it.message)
        }.addOnSuccessListener {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
            Log.d("TAG","SUCCESS------------------------===")
        }

       // uploadTask = storageRef.child("receipt.jpg").putFile(Uri.fromFile(File(path)));
    }
}