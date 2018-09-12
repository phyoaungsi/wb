package pas.com.mm.shoopingcart.order

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import butterknife.ButterKnife
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_basket.*

import pas.com.mm.shoopingcart.R
import pas.com.mm.shoopingcart.database.model.Model
import pas.com.mm.shoopingcart.database.model.OrderForm
import pas.com.mm.shoopingcart.order.adapter.NameValue
import pas.com.mm.shoopingcart.order.adapter.PaymentTypeAdapter
import butterknife.OnClick
import kotlinx.android.synthetic.main.content_basket.*
import pas.com.mm.shoopingcart.ItemGridView
import pas.com.mm.shoopingcart.database.model.UserProfile
import pas.com.mm.shoppingcart.order.BasketDataPresenter
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.annotation.NonNull
import com.facebook.share.internal.ShareInternalUtility.getPhotoUrls
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import android.support.v4.content.ContextCompat
import android.view.Gravity
import pas.com.mm.shoopingcart.util.FontUtil.setText
import android.widget.TextView
import pas.com.mm.shoopingcart.util.CloudStorageUtil
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class BasketActivity() : AppCompatActivity(), BasketDataCallBack {

    private val REQUEST_SELECT_PICTURE = 2

    private val REQUEST_IMAGE_CAPTURE = 1
    // private final var gridview: GridView? =null
    private var db = BasketDataPresenter()
    private var mQuantityText:TextView? = null
    private var mSubtotalText:TextView? = null

    var basketList : List<OrderForm> = ArrayList()
    var gridview:RecyclerView? =null
    var adapter: BasketAdapter?=null
    var mDeliveryTypeSpinner:Spinner?=null
    var mPaymentTypeAdapter:ArrayAdapter<String>?=null

    var confirmBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        setSupportActionBar(toolbar)

        gridview = findViewById<View>(R.id.recycle_basket) as RecyclerView
        mSubtotalText=findViewById<TextView>(R.id.subtotal_amount_label)
        mQuantityText=findViewById<TextView>(R.id.quantity_amount_label)
        mDeliveryTypeSpinner=findViewById<Spinner>(R.id.delivery_type_spinner)
        val items = ArrayList<OrderForm>()
        adapter = BasketAdapter(this, items)

        gridview?.adapter = adapter
        var mLayout:LinearLayoutManager=LinearLayoutManager(this)
       mLayout.isSmoothScrollbarEnabled=false
        gridview?.layoutManager = mLayout
        gridview?.isNestedScrollingEnabled=false
        val user = FirebaseAuth.getInstance().currentUser

        var ddlList = mutableListOf<NameValue>()
        var value:NameValue= NameValue()
        value.name="Transfer"
        value.value="TXN"
        var value1:NameValue=NameValue()
        value1.name="Cash On Delivery"
        value1.value="COD"
        ddlList.add(value)
        ddlList.add(value1)

        val paymentTypes = arrayOf("Money Transfer", "Cash On Delivery")
        mPaymentTypeAdapter= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,paymentTypes)
        mDeliveryTypeSpinner?.adapter=mPaymentTypeAdapter
        db.callback=this
        comfirm_order.setOnClickListener( { view-> db.checkout()})

        payment_upload.setOnClickListener({view-> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showImageChooserDialog()
        }
        })
        db.loadBasket(this, user?.uid);
        db.retrieveUserProfile(this)

    }


    override fun initDataLoaded(list: MutableList<OrderForm>) {

        basketList=list;

        if(list.size>0){
            itemlist.visibility=View.VISIBLE
            noitem.visibility=View.GONE
            for(i in 0 until basketList.size){
                db.getItemById(basketList[i],this)
            }
        }
        else
        {
            itemlist.visibility=View.GONE
            noitem.visibility=View.VISIBLE
        }

    }

    override fun onBasketRecyclerChange() {
         adapter?.updateData(basketList)
         var subtotal: Double =0.0
        var qty: Int =0
        for(order in basketList){
            subtotal += order.amount
            qty += order.quantity
        }
        mSubtotalText?.text=subtotal.toString()
        mQuantityText!!.text=qty.toString()
    }

    override fun receiveResult(model: Model) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun submit(view: View) {

      var    builder :AlertDialog.Builder =  AlertDialog.Builder(this)
        builder.setMessage(this.getString(R.string.order_completed))
        builder.setPositiveButton(R.string.ok){Dialog,  which ->
        ItemGridView.startActivity(this)}
        builder.setNegativeButton(R.string.cancel) { Dialog, which ->
        }
        var  dialog:AlertDialog = builder.create()
        dialog.show()
    }


    fun submit() {

        var    builder :AlertDialog.Builder =  AlertDialog.Builder(this)
        builder.setMessage(this.getString(R.string.order_completed))
        builder.setPositiveButton(R.string.ok){Dialog,  which ->
            ItemGridView.startActivity(this)}
        builder.setNegativeButton(R.string.cancel) { Dialog, which ->
        }
        var  dialog:AlertDialog = builder.create()
        dialog.show()
    }

    override fun successCheckout() {

        submit()
    }

    override fun setUserInfo(post: UserProfile) {
        lblUserName.setText(FirebaseAuth.getInstance().currentUser!!.displayName)
        lblAddress.setText(post.address)
        lblPhone.setText(post.phone)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun showImageChooserDialog() {
        val items = arrayOf<CharSequence>(resources.getString(R.string.take_photo), resources.getString(R.string.choose_from_libary), resources.getString(R.string.cancel))

        val title = TextView(this)
        title.text = "Add Photo!"
        title.setBackgroundColor(Color.BLACK)
        title.setPadding(10, 15, 15, 10)
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.WHITE)
        title.textSize = 22f

        val builder = AlertDialog.Builder(this)
        builder.setCustomTitle(title)

        builder.setItems(items) { dialog, item ->
            if (items[item] == resources.getString(R.string.take_photo)) {
                if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        this.requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
                    } else {
                        // dispatchTakePictureIntent();
                        openCameraIntent()
                    }
                } else {
                    Toast.makeText(this, this.getString(R.string.device_has_no_camera), Toast.LENGTH_SHORT).show()
                }
            } else if (items[item] == resources.getString(R.string.choose_from_libary)) {
                if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_SELECT_PICTURE)
                } else {
                    dispatchSelectImageFromGalleryIntent()
                }

            } else if (items[item] == resources.getString(R.string.cancel)) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun dispatchSelectImageFromGalleryIntent() {
        val intent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                REQUEST_SELECT_PICTURE)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(this.getPackageManager()) != null) {
            //Create a file to store the image
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this, "com.zila.zilamart.provider", photoFile!!)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    var imageFilePath: String=""
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        imageFilePath = image.getAbsolutePath()
        return image
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // mImagePanel.setVisibility(View.VISIBLE);
            // Bundle extras = data.getExtras();
            // Bitmap imageBitmap = (Bitmap) extras.get("data");

          //  val photoUrl = mPhotoGridAdapter.getPhotoUrls()
          //  photoUrl.add(imageFilePath)
           // mPhotoGridAdapter.updateData(photoUrl)
            //  mReceiptImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    val selectedImage = data.data
                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val c = this.getContentResolver().query(selectedImage, filePath, null, null, null)
                    c.moveToFirst()
                    val columnIndex = c.getColumnIndex(filePath[0])
                    val picturePath = c.getString(columnIndex)
                    c.close()

                   var util: CloudStorageUtil=CloudStorageUtil()
                   util.uploadFile(picturePath)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // dispatchTakePictureIntent();
                openCameraIntent()
            } else {
                Toast.makeText(this, getString(R.string.allow_camera_access_settings), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
