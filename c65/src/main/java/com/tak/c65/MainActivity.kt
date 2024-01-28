package com.tak.c65

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editView = findViewById<EditText>(R.id.editView)
        val button = findViewById<Button>(R.id.button)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if(isGranted) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${editView.text}"))
                startActivity(intent)
            } else {
                Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show()
            }
        }

        button.setOnClickListener {
            val status = ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE")
            if(status == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${editView.text}"))
                startActivity(intent)
            } else {
                permissionLauncher.launch("android.permission.CALL_PHONE")
            }
        }
    }
}


/**
 * C65
 *
 * <Call App>
 * 핸드폰에서 전화를 걸거나 받는 앱을 지칭함.
 * 얘가 워낙 잘 만들어진 Activity 다 보니,
 * Intent 를 생성해서 쟤를 띄우는게 그냥 낫고, 자주 쓰인다. 이말 같음
 *
 * 우리 앱에서 전화번호 데이터만 저쪽 액티비티에 넘겨서.
 * Call App의 액티비티의 인텐트를 발생시키면 된다 라는 얘기
 *
 * - 퍼미션 필요
 * - 인텐트의 액션 문자열을 Intent.ACTION_CALL 로 지정
 * - data 정보의 URL 은 tel: 으로 선언
 * - data 정보로 전화 번호 명시
 *
 */