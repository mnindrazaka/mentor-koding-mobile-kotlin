package com.example.aka.mentorkoding

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.SkillsQuery
import com.example.aka.mentorkoding.databinding.ActivitySearchBinding
import org.jetbrains.anko.support.v4.runOnUiThread

class SearchActivity : Fragment() {

    private lateinit var apolloClient: ApolloClient
    lateinit var binding: ActivitySearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_search, container, false)
        binding.editTextSkill.setOnItemClickListener { adapterView, view, i, l -> search(adapterView.getItemAtPosition(i) as String) }
        apolloClient = ApolloGateway(context!!).createClient()
        getSkill()
        return binding.root
    }

    private fun getSkill() {
        val skillsQuery = SkillsQuery.builder().build()
        apolloClient.query(skillsQuery).enqueue(object : ApolloCall.Callback<SkillsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, skills)
        binding.editTextSkill.setAdapter(adapter)
    }

    private fun search(skill: String) {
        val intent = Intent(activity, SearchResultActivity::class.java)
        intent.putExtra("skill", skill)
        startActivity(intent)
    }
}
