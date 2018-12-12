package com.example.aka.mentorkoding

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.UpdateUserMutation
import com.apollographql.apollo.sample.type.UserUpdate
import com.example.aka.mentorkoding.databinding.ActivityUpdateProfilePictureBinding
import java.io.ByteArrayOutputStream

class UpdateProfilePictureActivity : AppCompatActivity() {
    private lateinit var apolloClient : ApolloClient
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var binding : ActivityUpdateProfilePictureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile_picture)
        binding.buttonOpenCamera.setOnClickListener { dispatchTakePictureIntent() }
        binding.buttonSubmit.setOnClickListener { updateProfile() }
        apolloClient = ApolloGateway(this).createClient()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageViewPhoto.setImageBitmap(imageBitmap)
        }
    }

    private fun updateProfile() {
        val image64 = encodeImage ((binding.imageViewPhoto.drawable as BitmapDrawable).bitmap)
        val user = UserUpdate.builder().profilePic(image64).build()
        val updateUserMutation = UpdateUserMutation.builder().user(user).build()
        apolloClient.mutate(updateUserMutation).enqueue(object: ApolloCall.Callback<UpdateUserMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(response: Response<UpdateUserMutation.Data>) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Profile updated", Toast.LENGTH_SHORT).show()
                }
                moveToProfile()
            }
        })
    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun moveToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}
