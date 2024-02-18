package com.tak.c77

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : JobService() {

    override fun onCreate(){
        super.onCreate()
        Log.d("tak", "MyService...onCreate...")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("tak", "MyService...onDestroy...")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("tak", "MyService...onStartJob...")
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("tak", "MyService...onStopJob...")
        return false
    }
}
