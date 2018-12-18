package com.example.aka.mentorkoding

import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.UserQuery
import com.example.aka.mentorkoding.adapter.SkillAdapter
import com.example.aka.mentorkoding.databinding.ActivityMentorProfileBinding

class MentorProfileActivity : AppCompatActivity() {

    private lateinit var apolloClient: ApolloClient
    lateinit var binding: ActivityMentorProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mentor_profile)
        apolloClient = ApolloGateway(this).createClient()
        getProfile()
    }

    private fun getProfile() {
        val _id = intent.getStringExtra("_id")
        val userQuery = UserQuery.builder()._id(_id).build()
        apolloClient
            .query(userQuery)
            .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
            .enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onResponse(response: Response<UserQuery.Data>) {
                    binding.profile = response.data()?.user()
                    runOnUiThread {
                        setupRecyclerView()
                        setupPhoto(binding.profile?.profilePic())
                    }
                }
            })
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSkill.adapter = SkillAdapter(binding.profile!!.skills()!!) {}
        binding.recyclerViewSkill.layoutManager = LinearLayoutManager(this)
    }

    private fun setupPhoto(encodedImage : String?) {
        if (!encodedImage.isNullOrEmpty()) {
            val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
            binding.imageViewPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size))
        }
    }
}
