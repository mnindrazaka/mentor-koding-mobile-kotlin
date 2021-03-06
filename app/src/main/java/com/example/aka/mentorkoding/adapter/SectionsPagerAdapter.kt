package com.example.aka.mentorkoding.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.aka.mentorkoding.ProfileActivity
import com.example.aka.mentorkoding.SearchActivity

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            SearchActivity()
        } else {
            ProfileActivity()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}