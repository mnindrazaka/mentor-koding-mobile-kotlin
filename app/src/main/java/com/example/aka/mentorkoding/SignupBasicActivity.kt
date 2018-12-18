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
import com.apollographql.apollo.sample.CreateUserMutation
import com.apollographql.apollo.sample.type.UserInput
import com.example.aka.mentorkoding.databinding.ActivitySignupBasicBinding

class SignupBasicActivity : AppCompatActivity() {

    private lateinit var apolloClient : ApolloClient
    lateinit var binding : ActivitySignupBasicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_basic)
        binding.buttonSignup.setOnClickListener { signup() }
        binding.buttonCancel.setOnClickListener { moveToSignin() }
        apolloClient = ApolloGateway(this).createClient()
    }

    private fun signup() {
        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()
        val confirm_password = binding.editTextConfirmPassword.text.toString()

        if (!password.equals(confirm_password)) {
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
            binding.editTextPassword.setText("")
            binding.editTextConfirmPassword.setText("")
            binding.editTextPassword.requestFocus()
            return
        }

        val name = binding.editTextName.text.toString()
        val phone = binding.editTextPhone.text.toString()
        val address = binding.editTextAddress.text.toString()
        val user = UserInput
            .builder()
            .username(username)
            .password(password)
            .name(name)
            .phone(phone)
            .address(address)
            .build()

        val createUserMutation = CreateUserMutation.builder().user(user).build()
        apolloClient
            .mutate(createUserMutation)
            .enqueue(object : ApolloCall.Callback<CreateUserMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(response: Response<CreateUserMutation.Data>) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Signup Success, Please Login", Toast.LENGTH_SHORT).show()
                    }
                    moveToSignin()
                }
            })
    }

    private fun moveToSignin() {
        val intent = Intent(this, SigninActivity::class.java)
        startActivity(intent)
    }
}
