package com.example.aka.mentorkoding.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.apollographql.apollo.sample.SearchQuery
import com.apollographql.apollo.sample.UserQuery
import com.example.aka.mentorkoding.databinding.LayoutMentorBinding
import com.example.aka.mentorkoding.databinding.LayoutSkillBinding
import org.jetbrains.anko.sdk27.coroutines.onClick

class MentorAdapter(val mentors : List<SearchQuery.Search>, val listener: (Int) -> Unit) : RecyclerView.Adapter<MentorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutMentorBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mentors.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.mentor = mentors[position]
        holder.itemView.onClick {
            listener(position)
        }
    }

    class ViewHolder(val binding: LayoutMentorBinding) : RecyclerView.ViewHolder(binding.root)
}