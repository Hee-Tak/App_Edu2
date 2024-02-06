package com.tak.c72

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    lateinit var binder : MyService.MyBinder

    val connection: ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            binder = p1 as MyService.MyBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            TODO("Not yet implemented")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<ImageView>(R.id.startButton)
        val stopButton = findViewById<ImageView>(R.id.stopButton)

        startButton.setOnClickListener {
            binder.startMusic()
            startButton.setEnabled(false)
            stopButton.setEnabled(true)
        }

        stopButton.setOnClickListener {
            binder.stopMusic()
            startButton.setEnabled(true)
            stopButton.setEnabled(false)
        }

        val intent = Intent(this, MyService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}


/**
 * c72) bindService
 *
 * - bindService() 는 서비스가 실행되면서 자신을 실행시킨 곳에 객체를 바인딩한다는 의미
 *
 *
 *              Activity                                            Service
 *          -----------------                                   -----------------
 *          |               |          bindService              |               |
 *          |       | ------|-----------------------------------|---→           |
 *          |       |       |                                   |               |
 *          |       |       |           객체 바인딩         ============          |
 *          |       |←------|-----------------------------|           |         |
 *          |       | ------------------------------------|-→ a() { } |         |
 *          |       | ------------------------------------|-→ b() { } |         |
 *          |       |       |                             |===========|         |
 *          |       ↓       |                                   |               |
 *          |               |                                   |               |
 *          -----------------                                   -----------------
 *
 *          이런 식으로 두 컴포넌트 간에 매개변수나 리턴값으로 데이터를 주고 받을 수 있다.
 *
 * - 서비스의 라이프사이클 함수중 onBind() 함수가 실행
 *
 *          class MyBinder: Binder() {
 *              fun funA(arg: Int) {
 *                                      //funA, funB 이쪽은 개발자 마음대로 작성(매개변수, 리턴, 등등 모두)
 *              }
 *
 *              fun funB(arg: Int): Int {
 *                  return arg * arg    //외부에서 이 함수들 (funA, funB....)을 호출하면서 서비스랑 연동하는 것
 *              }
 *          }
 *
 *          override fun onBind(intent: Intent): IBinder? {
 *              return MyBinder()
 *          }
 *          // bindService() 에 의해서 onBind()가 호출이 되면, (IBinder) 객체를 리턴시켜 줘야되고, 이 객체가 액티비티 같은 곳에 전달된다는 것.
 *
 *
 *
 * - ServiceConnection 을 구현한 객체 준비              //서비스랑 바인딩 방법으로 연결이 됐다. 그런 의미
 * - onServiceConnected() 함수의 매개변수로 Bind 객체 전달
 *
 *          val connection: ServiceConnection = object : ServiceConnection {
 *              override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
 *                  serviceBinder = service as MyService.MyBinder
 *              }   //서비스랑 연결이 됐을 때, onServiceConnected 가 자동 호출이 됨.
 *
 *              override fun onServiceDisconnected(name: ComponentName?) {
 *
 *              }   // 바인딩이 끊어지면 onServiceDisconnected 함수가 자동호출 됨.
 *          }
 *
 */