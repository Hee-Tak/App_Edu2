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
 */