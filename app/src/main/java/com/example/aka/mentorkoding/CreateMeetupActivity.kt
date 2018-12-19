package com.example.aka.mentorkoding

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.CreateMeetupMutation
import com.apollographql.apollo.sample.type.MeetupInput
import com.example.aka.mentorkoding.databinding.ActivityCreateMeetupBinding
import android.databinding.adapters.TimePickerBindingAdapter.getMinute
import android.databinding.adapters.TimePickerBindingAdapter.getHour



class CreateMeetupActivity : AppCompatActivity() {

    private lateinit var apolloClient : ApolloClient
    lateinit var binding : ActivityCreateMeetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_meetup)
        binding.buttonSubmit.setOnClickListener { submit() }
        binding.username = intent.getStringExtra("username")
        apolloClient = ApolloGateway(this).createClient()
    }

    private fun submit() {
        val topic = binding.editTextTopic.text.toString()
        val mentorId = intent.getStringExtra("_id")
        val datetime = getDateTime()
        val detailPlace = binding.editTextDetailPlace.text.toString()
        val meetup = MeetupInput
            .builder()
            .topic(topic)
            .mentorId(mentorId)
            .datetime(datetime)
            .detailPlace(detailPlace)
            .lat(0.0)
            .lng(0.0)
            .build()
        val createMeetupMutation = CreateMeetupMutation.builder().meetup(meetup).build()
        apolloClient.mutate(createMeetupMutation).enqueue(object: ApolloCall.Callback<CreateMeetupMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(response: Response<CreateMeetupMutation.Data>) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Meetup Created", Toast.LENGTH_SHORT).show()
                    moveToMeetupList()
                }
            }
        })
    }

    private fun getDateTime() : String {
        val date =  binding.datepicker.year.toString() + "-" + binding.datepicker.month.toString() + "-" + binding.datepicker.dayOfMonth.toString()
        val time = binding.editTextTime.text.toString()
        return date + "T" + time
    }

    private fun moveToMeetupList() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
