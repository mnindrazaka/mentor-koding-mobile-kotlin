package com.example.aka.mentorkoding

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.aka.mentorkoding.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container.adapter = SectionsPagerAdapter(supportFragmentManager)
    }
}
