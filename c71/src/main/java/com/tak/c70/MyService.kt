package com.tak.c70

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import java.lang.Exception

class MyService : Service() {

    lateinit var player: MediaPlayer //음원 뿐만 아니라, 영상까지 플레이 시켜줄 수 있는 클래스

    var receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?){
            val mode = intent?.getStringExtra("mode")
            if(mode != null){
                if(mode == "start"){
                    try {
                        if(player != null && player.isPlaying){
                            player.stop()
                            player.release()
                        }
                        player = MediaPlayer.create(context, R.raw.veaceslav_draganov_miracle)
                        player.start()

                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                } else if(mode == "stop"){
                    if(player != null && player.isPlaying){
                        player.stop()
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        registerReceiver(receiver, IntentFilter("PLAY_TO_SERVICE"))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}