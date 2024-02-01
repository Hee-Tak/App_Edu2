package com.tak.c68

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * 파트18 - BroadcastReceiver 를 이해해 보자.
 * 67강 : Concepts of BroadcastReceiver
 *
 * 안드로이드 4개 컴포넌트 중에 하나인 BroadcastReceiver   => api 적으로는 가장 간단한 컴포넌트
 * 지금까지 살펴본 컴포넌트는 화면 출력 목적으로 하는 Activity Component 였음
 *
 *
 * <BroadcastReceiver>
 * - 이벤트 모델로 실행되는 컴포넌트
 * - BroadcastReceiver 를 상속받은 클래스 작성
 */