package com.tak.c68

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("tak", "receiver.....")
    }
}

/**
 * 로그는 Logcat com.tak 에서 확인
 */