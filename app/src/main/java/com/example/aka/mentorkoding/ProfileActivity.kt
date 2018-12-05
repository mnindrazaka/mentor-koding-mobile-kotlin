package com.example.aka.mentorkoding

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.ProfileQuery
import com.example.aka.mentorkoding.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var apolloClient : ApolloClient
    lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.buttonUpdate.setOnClickListener { moveToUpdateProfile() }
        binding.buttonLogout.setOnClickListener { logout() }

        apolloClient = ApolloGateway(this).createClient()
        getProfile()
    }

    private fun getProfile() {
        val profileQuery = ProfileQuery.builder().build()
        apolloClient.query(profileQuery).enqueue(object : ApolloCall.Callback<ProfileQuery.Data>() {
            override fun onFailure(e: ApolloException) {}
            override fun onResponse(response: Response<ProfileQuery.Data>) {
                binding.profile = response.data()?.profile()
            }
        })
    }

    private fun moveToUpdateProfile() {
        val intent = Intent(this, UpdateProfileActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        clearToken()
        moveToLogin()
    }

    private fun clearToken() {
        getSharedPreferences("auth", Context.MODE_PRIVATE).edit().remove("token").apply()
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
