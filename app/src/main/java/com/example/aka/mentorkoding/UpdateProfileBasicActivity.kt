package com.example.aka.mentorkoding

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.ProfileQuery
import com.apollographql.apollo.sample.UpdateUserMutation
import com.apollographql.apollo.sample.type.UserUpdate
import com.example.aka.mentorkoding.databinding.ActivityUpdateProfileBasicBinding

class UpdateProfileBasicActivity : AppCompatActivity() {

    private lateinit var apolloClient : ApolloClient
    lateinit var binding : ActivityUpdateProfileBasicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile_basic)
        binding.buttonSubmit.setOnClickListener { updateProfile() }
        binding.buttonCancel.setOnClickListener { moveToProfile() }
        apolloClient = ApolloGateway(this).createClient()
        getProfile()
    }

    private fun getProfile() {
        val profileQuery = ProfileQuery.builder().build()
        apolloClient.query(profileQuery).enqueue(object : ApolloCall.Callback<ProfileQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(response: Response<ProfileQuery.Data>) {
                binding.profile = response.data()?.profile()
            }
        })
    }

    private fun updateProfile() {
        val name = binding.editTextName.text.toString()
        val phone = binding.editTextPhone.text.toString()
        val address = binding.editTextAddress.text.toString()
        val user = UserUpdate
            .builder()
            .name(name)
            .phone(phone)
            .address(address)
            .build()

        val updateUserMutation = UpdateUserMutation.builder().user(user).build()
        apolloClient
            .mutate(updateUserMutation)
            .enqueue(object : ApolloCall.Callback<UpdateUserMutation.Data>() {
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

    private fun moveToProfile() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
