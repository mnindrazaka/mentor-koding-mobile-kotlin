package com.example.aka.mentorkoding

import android.support.v4.app.Fragment
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.ProfileQuery
import com.example.aka.mentorkoding.adapter.SkillAdapter
import com.example.aka.mentorkoding.databinding.ActivityProfileBinding
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apollographql.apollo.sample.UpdateUserMutation
import com.apollographql.apollo.sample.type.UserUpdate
import org.jetbrains.anko.support.v4.runOnUiThread


class ProfileActivity : Fragment() {

    private lateinit var apolloClient: ApolloClient
    lateinit var binding: ActivityProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_profile, container, false)
        binding.imageViewPhoto.setOnClickListener { moveToUpdateProfile(UpdateProfilePictureActivity::class.java) }
        binding.cardViewBasic.setOnClickListener { moveToUpdateProfile(UpdateProfileBasicActivity::class.java) }
        binding.cardViewSkill.setOnClickListener { moveToUpdateProfile(UpdateProfileSkillActivity::class.java) }
        binding.cardViewSosmed.setOnClickListener { moveToUpdateProfile(UpdateProfileSosmedActivity::class.java) }
        binding.switchMentor.setOnCheckedChangeListener { compoundButton, b -> updateMentorStatus(b)  }
        binding.buttonLogout.setOnClickListener { logout() }

        apolloClient = ApolloGateway(context!!).createClient()
        getProfile()
        return binding.root
    }

    private fun getProfile() {
        val profileQuery = ProfileQuery.builder().build()
        apolloClient
            .query(profileQuery)
            .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
            .enqueue(object : ApolloCall.Callback<ProfileQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    runOnUiThread {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onResponse(response: Response<ProfileQuery.Data>) {
                    binding.profile = response.data()?.profile()
                    runOnUiThread {
                        setupRecyclerView()
                        setupPhoto(binding.profile?.profilePic())
                    }
                }
            })
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSkill.adapter = SkillAdapter(binding.profile!!.skills()!!) {}
        binding.recyclerViewSkill.layoutManager = LinearLayoutManager(activity)
    }

    private fun setupPhoto(encodedImage : String?) {
        if (!encodedImage.isNullOrEmpty()) {
            val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
            binding.imageViewPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size))
        }
    }

    private fun moveToUpdateProfile(activityUpdate : Class<*>) {
        val intent = Intent(activity, activityUpdate)
        startActivity(intent)
    }

    private fun logout() {
        clearToken()
        moveToLogin()
    }

    private fun clearToken() {
        context!!.getSharedPreferences("auth", Context.MODE_PRIVATE).edit().remove("token").apply()
    }

    private fun moveToLogin() {
        val intent = Intent(context, SigninActivity::class.java)
        startActivity(intent)
    }

    private fun updateMentorStatus(isMentor: Boolean) {
        val user = UserUpdate.builder().isMentor(isMentor).build()
        val updateUserMutation = UpdateUserMutation.builder().user(user).build()
        apolloClient
            .mutate(updateUserMutation)
            .enqueue(object : ApolloCall.Callback<UpdateUserMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    runOnUiThread {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(response: Response<UpdateUserMutation.Data>) {
                    runOnUiThread {
                        Toast.makeText(activity, "Mentor Status Changed", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}
