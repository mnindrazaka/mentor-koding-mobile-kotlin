package com.example.aka.mentorkoding.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class SkillAdapter(val skills : List<String>) : RecyclerView.Adapter<SkillAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TextView(parent.context)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return skills.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text_view_skill.text = skills[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_view_skill = view as TextView
    }
}