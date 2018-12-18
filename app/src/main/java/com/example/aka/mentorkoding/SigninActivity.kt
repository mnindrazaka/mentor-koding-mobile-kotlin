package com.example.aka.mentorkoding

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.LoginQuery
import com.example.aka.mentorkoding.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySigninBinding
    private lateinit var apolloClient : ApolloClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apolloClient = ApolloGateway(applicationContext).createClient()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signin)
        binding.buttonSignin.setOnClickListener { signin() }
        binding.buttonSignup.setOnClickListener { signup() }
        checkAuth()
    }

    private fun signin() {
        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()
        val loginQuery = LoginQuery.builder().username(username).password(password).build()
        apolloClient.query(loginQuery).enqueue(object : ApolloCall.Callback<LoginQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(response: Response<LoginQuery.Data>) {
                saveToken(response.data()?.login())
                runOnUiThread {
                    clearInput()
                }
            }
        })
    }

    private fun saveToken(token: String?) {
        if (!token.isNullOrEmpty()) {
            val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("token", token)
            editor.apply()
            moveToProfile()
        }
    }

    private fun clearInput() {
        binding.editTextPassword.setText("")
    }

    private fun checkAuth() {
        if (getSharedPreferences("auth", Context.MODE_PRIVATE).contains("token")) {
            moveToProfile()
        }
    }

    private fun signup() {
        val intent = Intent(this, SignupBasicActivity::class.java)
        startActivity(intent)
    }

    private fun moveToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}
