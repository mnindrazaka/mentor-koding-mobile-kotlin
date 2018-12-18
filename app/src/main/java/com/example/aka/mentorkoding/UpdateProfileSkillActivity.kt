package com.example.aka.mentorkoding

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.ProfileQuery
import com.apollographql.apollo.sample.SkillsQuery
import com.apollographql.apollo.sample.UpdateUserMutation
import com.apollographql.apollo.sample.type.UserUpdate
import com.example.aka.mentorkoding.adapter.SkillAdapter
import com.example.aka.mentorkoding.databinding.ActivityUpdateProfileSkillBinding

class UpdateProfileSkillActivity : AppCompatActivity() {

    private lateinit var apolloClient: ApolloClient
    lateinit var binding: ActivityUpdateProfileSkillBinding
    lateinit var profileSkills: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile_skill)
        binding.editTextSkill.setOnItemClickListener { adapterView, view, i, l -> addSkill(adapterView.getItemAtPosition(i) as String) }
        binding.buttonSubmit.setOnClickListener { updateProfile() }
        binding.buttonCancel.setOnClickListener { moveToProfile() }

        apolloClient = ApolloGateway(this).createClient()
        getSkill()
        getProfile()
    }

    private fun getSkill() {
        val skillsQuery = SkillsQuery.builder().build()
        apolloClient.query(skillsQuery).enqueue(object : ApolloCall.Callback<SkillsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(response: Response<SkillsQuery.Data>) {
                runOnUiThread {
                    setupAutocomplete(response.data()?.skills()!!)
                }
            }
        })
    }

    private fun setupAutocomplete(skills: List<String>) {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, skills)
        binding.editTextSkill.setAdapter(adapter)
    }

    private fun getProfile() {
        val profileQuery = ProfileQuery.builder().build()
        apolloClient
            .query(profileQuery)
            .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
            .enqueue(object : ApolloCall.Callback<ProfileQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onResponse(response: Response<ProfileQuery.Data>) {
                    runOnUiThread {
                        setupRecyclerView(response.data()?.profile()!!.skills()!!)
                    }
                }
            })
    }

    private fun setupRecyclerView(skills: List<String>) {
        profileSkills = ArrayList(skills)
        binding.recyclerViewSkill.adapter = SkillAdapter(profileSkills) { i -> removeSkill(i) }
        binding.recyclerViewSkill.layoutManager = LinearLayoutManager(this)
    }

    private fun addSkill(skill: String) {
        if (profileSkills.indexOf(skill) == -1) {
            profileSkills.add(skill)
            binding.recyclerViewSkill.adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Skill already added", Toast.LENGTH_SHORT).show()
        }
        binding.editTextSkill.setText("")
    }

    private fun removeSkill(index: Int) {
        profileSkills.removeAt(index)
        binding.recyclerViewSkill.adapter.notifyDataSetChanged()
        Toast.makeText(this, "Skill removed", Toast.LENGTH_SHORT).show()
    }

    private fun updateProfile() {
        val user = UserUpdate
            .builder()
            .skills(profileSkills)
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
