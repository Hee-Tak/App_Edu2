package com.tak.c72

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
 *
 *              }
 *
 *              fun funB(arg: Int): Int {
 *                  return arg * arg
 *              }
 *          }
 *
 *          override fun onBind(intent: Intent): IBinder? {
 *              return MyBinder()
 *          }
 *          // bindService() 에 의해서 onBind()가 호출이 되면, (IBinder) 객체를 리턴시켜 줘야되고, 이 객체가 액티비티 같은 곳에 전달된다는 것.
 *
 */