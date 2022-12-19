package com.example.loadapp

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.loadapp.databinding.ActivityMainBinding

class Main : AppCompatActivity() {
    private var url: String? = null
    private  var downloadID: Long =0
    private var fileName: String? = null
    lateinit var binding:ActivityMainBinding
    lateinit var receiver: BroadcastReceiver

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        broadCast()
        registerReceiver(receiver , IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        radioClicks()





    }

    private fun broadCast(){
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

                val query = DownloadManager.Query()
                query.setFilterById(id!!)

                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    var downloadStatus = "Failed"
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(index)) downloadStatus = "Success"

                    Toast.makeText(applicationContext,"Downloaded Successfully", Toast.LENGTH_LONG).show()

                    binding.loading.buttonState = ButtonState.Completed
                    Notification.createSampleDataNotification(
                        context=applicationContext,
                        title=getString(R.string.notification_title),
                        message =getString(R.string.app_name) ,
                        autoCancel = true,
                        status = downloadStatus,
                        fileName=fileName!!
                    )

                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun radioClicks(){
        binding.loading.setOnClickListener {
            when{
                binding.glideButton.isChecked->{
                    url= getString(R.string.Glide)
                    fileName = getString(R.string.glide_text)
                    binding.loading.buttonState=ButtonState.Loading
                    download()
                }
                binding.loadAppButton.isChecked->{
                    url=getString(R.string.LoadApp)
                    fileName= getString(R.string.load_app_text)
                    binding.loading.buttonState=ButtonState.Loading
                    download()

                }
                binding.retrofitButton.isChecked -> {
                    url = getString(R.string.Retrofit)
                    fileName = getString(R.string.retrofit_text)
                    binding.loading.buttonState = ButtonState.Loading
                    download()
                }
                else -> {
                    Toast.makeText(this, "Select an option", Toast.LENGTH_LONG).show()
                        .toString()

                }

            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_name))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
           downloadID= downloadManager.enqueue(request)

    }

}
