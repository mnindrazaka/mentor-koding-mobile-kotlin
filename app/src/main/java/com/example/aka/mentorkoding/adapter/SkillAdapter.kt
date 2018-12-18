package com.example.aka.mentorkoding.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.aka.mentorkoding.databinding.LayoutSkillBinding
import org.jetbrains.anko.sdk27.coroutines.onClick

class SkillAdapter(val skills : List<String>, val listener: (Int) -> Unit) : RecyclerView.Adapter<SkillAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutSkillBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return skills.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.skill = skills[position]
        holder.itemView.onClick {
            listener(position)
        }
    }

    class ViewHolder(val binding: LayoutSkillBinding) : RecyclerView.ViewHolder(binding.root)
}