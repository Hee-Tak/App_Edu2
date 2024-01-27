package com.tak.c64

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * C64
 * <Gallery App>
 *
 *
 * - 인텐트로 갤러리앱의 이미지 목록 화면을 출력
 *          val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
 *          intent.type = "image/*"
 *          requestActivity.launch(intent)
 */