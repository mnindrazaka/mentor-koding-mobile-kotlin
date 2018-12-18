package com.example.aka.mentorkoding.adapter

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import com.apollographql.apollo.sample.SearchQuery
import com.apollographql.apollo.sample.UserQuery
import com.example.aka.mentorkoding.databinding.LayoutMentorBinding
import com.example.aka.mentorkoding.databinding.LayoutSkillBinding
import org.jetbrains.anko.sdk27.coroutines.onClick

class MentorAdapter(val mentors : List<SearchQuery.Search>, val listener: (SearchQuery.Search) -> Unit) : RecyclerView.Adapter<MentorAdapter.ViewHolder>() {

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
            listener(mentors[position])
        }

        if (!mentors[position].profilePic().isNullOrEmpty()) {
            val decodedString = Base64.decode(mentors[position].profilePic(), Base64.DEFAULT)
            holder.binding.imageViewPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size))
        }
    }

    class ViewHolder(val binding: LayoutMentorBinding) : RecyclerView.ViewHolder(binding.root)
}