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
import com.apollographql.apollo.sample.LoginQuery
import com.example.aka.mentorkoding.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var apolloClient : ApolloClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apolloClient = ApolloGateway(applicationContext).createClient()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.buttonLogin.setOnClickListener { login() }
        checkAuth()
    }

    private fun login() {
        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()
        val loginQuery = LoginQuery.builder().username(username).password(password).build()
        apolloClient.query(loginQuery).enqueue(object : ApolloCall.Callback<LoginQuery.Data>() {
            override fun onFailure(e: ApolloException) {}
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

    private fun moveToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}
