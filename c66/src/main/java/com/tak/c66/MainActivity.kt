package com.tak.c66

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * C66
 * <Camera App>
 *
 * - 카메라 앱을 연동하여 사진을 촬영하고 결과를 되돌려 받는 방법은 두가지 방법
 * - 사진 데이터 획득 방법       => 이거는 카메라앱에서 파일을 저장하지 않는다. 카톡에서 기능 써보면 감 바로 온다.
 * - 파일 공유 방법
 *
 *
 *              App  ------------Intent-----------> Activity
 *                   <------------Data------------
 *
 */