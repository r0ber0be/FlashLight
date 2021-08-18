package com.example.flashlight

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var cameraM: CameraManager
    private lateinit var powerBtn:ImageButton
    var isFlash = false


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        powerBtn = findViewById(R.id.power)
        cameraM = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        powerBtn.setOnClickListener { flashLightOnRoOff(it)}

        val button = findViewById<View>(R.id.button)
        button.isEnabled = false

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),123)
        }
        else
            button.isEnabled = true

        var cm = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        button.setOnClickListener {
            if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                var light = false
                var s = "101010101010101010101010"
                for(i in s.indices){
                    light = s[i] == '1'
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        cm.setTorchMode(cm.cameraIdList[0],light)
                    }
                    Thread.sleep(50)
                }
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val button = findViewById<View>(R.id.button)
        if(requestCode==123 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            button.isEnabled = true

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun flashLightOnRoOff(view: View?) {
        if(!isFlash) {
            val cameraListId = cameraM.cameraIdList[0]
            cameraM.setTorchMode(cameraListId, true)
            isFlash = true
            powerBtn.setImageResource(R.drawable.ic_power_on)
            textMessage("Lanterna ligada", this)
        }
        else {
            val cameraListId = cameraM.cameraIdList[0]
            cameraM.setTorchMode(cameraListId, false)
            isFlash = false
            powerBtn.setImageResource(R.drawable.ic_power_off)
            textMessage("Lanterna desligada", this)
        }
    }

    private fun textMessage(s: String, c: Context) {
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show()
    }
}