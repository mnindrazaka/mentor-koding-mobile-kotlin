package com.example.aka.mentorkoding

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myUI = MainActivityUI()
        myUI.setContentView(this)
    }

    class MainActivityUI : AnkoComponent<MainActivity> {

        lateinit var goButton : Button

        override fun createView(ui: AnkoContext<MainActivity>) = ui.apply {
            verticalLayout {
                gravity = Gravity.CENTER

                textView {
                    text = "Username"
                }
                editText {
                    hint = "Enter Username"
                }
                goButton = button {
                    text = "Goto Login"
                }
            }
        }.view
    }
}
