package com.example.userpermissiondemo.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userpermissiondemo.R
import com.example.userpermissiondemo.adapter.AdapterImage
import com.example.userpermissiondemo.database.dbHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_second.*
import java.io.ByteArrayOutputStream


class SecondActivity : AppCompatActivity() {

    private val REQUEST_CODE = 101
    private val REQUEST_IMAGE_CODE = 102
    lateinit var view : View
    var imageList = ArrayList<Bitmap>()
    lateinit var myAdapter : AdapterImage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        init()


    }

    private fun showImages() {

        addImagesFromDbToList()
        displayImages()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        dbHelper().deleteImages()
//    }

    private fun init() {
//        button_camera.setOnClickListener(this)
//        button_gallery.setOnClickListener(this)
        button_show_fragment.setOnClickListener{
            val dialog = BottomSheetDialog(this)
            view = getLayoutInflater().inflate(R.layout.bottom_dialog, null)
               dialog.setContentView(view)
                dialog.setCancelable(false)
                dialog.show()
            showImages()
               var close = view.findViewById<ImageView>(R.id.imageView_close)
            var camera = view.findViewById<Button>(R.id.button_camera)
            var gallery = view.findViewById<Button>(R.id.button_gallery)
//                imageView_close.setOnClickListener{
//                    dialog.dismiss()
//                }
            close.setOnClickListener {
                dialog.dismiss()
            }
            camera.setOnClickListener {
                checkCameraPermission()
            }
            gallery.setOnClickListener {
                setImageFromGallery()
            }

        }
        //imageView_close.setOnClickListener(this)
    }


//    override fun onClick(v: View) {
//        when(v.id){
//            R.id.button_camera->{
//                checkCameraPermission()
//
//            }
//            R.id.button_gallery->{
//                setImageFromGallery()
//            }
//            R.id.button_show_fragment->{
//                val dialog = BottomSheetDialog(this)
//             // var view = LayoutInflater.inflate(R.layout.bottom_dialog, null)
//                dialog.setContentView(R.layout.bottom_dialog)
//                dialog.setCancelable(false)
//                dialog.show()
////                var close = findViewById<ImageView>(R.id.imageView_close)
////                close.setOnClickListener{
////                    dialog.dismiss()
////                }
//            }
//
//        }
//    }



    private fun checkCameraPermission() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if(permission==PackageManager.PERMISSION_GRANTED){
            openCamera()
        }
        else
            requestCameraPermission()
    }

    fun openCamera(){
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE)
    }

    fun requestCameraPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            REQUEST_CODE->{
//                Toast.makeText(this, grantResults.toString(), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun setImageFromGallery() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //var imageView1 = view.findViewById<ImageView>(R.id.imageView_show)
        when(requestCode){
            REQUEST_CODE->{
                var image1 = data!!.extras!!.get("data") as Bitmap
               // val dialog = BottomSheetDialog(this)
                //imageList.add(image1)
               // addListToAdapter(imageList)
                addImagesToDatabase(image1)
               // imageView1.setImageBitmap(image1)
            }
            REQUEST_IMAGE_CODE ->{
                if(resultCode==Activity.RESULT_OK){
                    var image1 = data!!.data
                    //imageView1.setImageURI(data!!.data)
                    //val imageUri: Uri? = data.data
                    val bitmapImage = MediaStore.Images.Media.getBitmap(this.contentResolver, image1)
                    addImagesToDatabase(bitmapImage)
//                    imageList.add(bitmapImage)
//                    addListToAdapter(imageList)
                }

            }
        }

    }


//    fun addListToAdapter( mlist : ArrayList<Bitmap>){
////         myAdapter = AdapterImage(this, mlist)
////        var recycler_view1 = view.findViewById<RecyclerView>(R.id.recycler_view_images)
////        recycler_view1.adapter = myAdapter
////        recycler_view1.layoutManager = GridLayoutManager(this, 3)
////    }

    fun addImagesToDatabase(bitMapImage : Bitmap){
        val bos = ByteArrayOutputStream()
        bitMapImage.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bArray: ByteArray = bos.toByteArray()
        dbHelper().addImages(bArray)
        imageList.clear()
        myAdapter = AdapterImage(this, imageList)
        myAdapter.notifyDataSetChanged()
        addImagesFromDbToList()
        displayImages()
    }

    fun addImagesFromDbToList(){
        var byteArrayList = dbHelper().getImages()
        for(i in 0 until byteArrayList.size){
            val bitmap = BitmapFactory.decodeByteArray(byteArrayList[i], 0, byteArrayList[i].size)
            imageList.add(bitmap)
        }
    }

    fun displayImages(){
        myAdapter = AdapterImage(this, imageList)
        var recycler_view1 = view.findViewById<RecyclerView>(R.id.recycler_view_images)
        recycler_view1.adapter = myAdapter
       // recycler_view1.layoutManager = GridLayoutManager(this, 3)
        recycler_view1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

}
