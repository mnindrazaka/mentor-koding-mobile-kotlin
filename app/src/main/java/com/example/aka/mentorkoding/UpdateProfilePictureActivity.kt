package com.example.aka.mentorkoding

import android.content.pm.PackageManager
import android.graphics.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class UpdateProfilePictureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile_picture)
    }

    private fun openCamera() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

        }
    }
}
