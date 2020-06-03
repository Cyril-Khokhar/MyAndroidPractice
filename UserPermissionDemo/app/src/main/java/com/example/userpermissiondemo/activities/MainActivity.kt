package com.example.userpermissiondemo.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.userpermissiondemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val CAMERA_REQUEST_CODE = 100
    private val MULTIPLE_REQUEST_CODE = 200
    var listPermission : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // init()
    }

    private fun init() {
        button_single.setOnClickListener(this)
        button_multiple.setOnClickListener(this)
    }

    override fun onClick(view: View) {
       when(view.id){
            R.id.button_single ->{
                checkCameraPermission()
            }
           R.id.button_multiple ->{
                checkMultiplePermission()
           }

       }
    }

    private fun checkMultiplePermission(){
        var permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        for(permission in permissions){
            if(ContextCompat.checkSelfPermission(this, permission)==PackageManager.PERMISSION_DENIED){
                listPermission.add(permission)
            }
            requestMultiplePermission()
        }
    }

    private fun requestMultiplePermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE), MULTIPLE_REQUEST_CODE)
    }

    private fun openCamera(){
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun checkCameraPermission(){
        var permission = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.CAMERA)
        if(permission!=PackageManager.PERMISSION_GRANTED){
            requestCameraPermission()
        }
        else
            openCamera()
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){

            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty() || grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    checkCameraPermission()
                }
                else{
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    openCamera()
                }
            }
            MULTIPLE_REQUEST_CODE->{

                Toast.makeText(this, "Multiple Permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
