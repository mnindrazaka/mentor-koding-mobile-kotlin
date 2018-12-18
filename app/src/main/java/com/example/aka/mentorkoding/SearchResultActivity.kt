package com.example.aka.mentorkoding

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.SearchQuery
import com.example.aka.mentorkoding.adapter.MentorAdapter
import com.example.aka.mentorkoding.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {

    private lateinit var apolloClient: ApolloClient
    lateinit var binding: ActivitySearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)

        apolloClient = ApolloGateway(this).createClient()
        getMentor()
    }

    private fun getMentor() {
        val skill = intent.getStringExtra("skill")
        val searchQuery = SearchQuery.builder().skill(skill).build()
        apolloClient.query(searchQuery).enqueue(object: ApolloCall.Callback<SearchQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(response: Response<SearchQuery.Data>) {
                runOnUiThread {
                    setupRecyclerView(response.data()!!.search()!!)
                }
            }
        })
    }

    private fun setupRecyclerView(mentors: MutableList<SearchQuery.Search>) {
        binding.recyclerViewMentor.adapter = MentorAdapter(mentors) { }
        binding.recyclerViewMentor.layoutManager = LinearLayoutManager(this)
    }
}
