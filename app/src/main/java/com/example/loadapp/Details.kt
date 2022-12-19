package com.example.loadapp

import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.loadapp.databinding.ActivityDetailsBinding

class Details : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_details)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

        binding.fileName.text = intent.getStringExtra("fileName")
        binding.statusText.text = intent.getStringExtra("status")

        if (binding.statusText.text == "Success") binding.statusText.setTextColor(getColor(R.color.green))
        else binding.statusText.setTextColor(getColor(R.color.red))


        binding.okButton.setOnClickListener {
            finish()
        }

    }
}