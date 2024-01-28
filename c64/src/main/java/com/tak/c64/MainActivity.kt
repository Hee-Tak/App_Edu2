package com.tak.c64

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val button = findViewById<Button>(R.id.button)

        //인텐트 발생시켜야하니까
        val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            //여기서부터의 영역은 갤러리앱에 들어와서 실행되는 영역
            //여기서 사진 데이터를 얻어와서 화면에 뿌리는 프로그램을 작성해보자
            try {
                val option = BitmapFactory.Options()
                option.inSampleSize = 5

                val inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                bitmap?.let {//실제 비트맵 객체가 만들어 졌다면 화면출력해야지
                    imageView.setImageBitmap(bitmap)
                } ?: let {

                }


            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            launcher.launch(intent)
        }
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
 *
 * Intent 로 발생 -> Activity -> id 획득 -> Provider -> data 획득
 *
 *
 * - OOM 문제가 발생할 수 있는 상황 (OutOfMemoryException) (-> 큰 이미지를 사이즈 줄여서 로딩해야하는 상황) (화면에 나오는 사이즈가 아니라 아예 데이터 사이즈를 줄여서 로딩해야 한다)
 * - BitmapFactory.Option 객체의 inSampleSize 값을 지정해 데이터 사이즈를 줄여서 로딩
 *          val option = BitmapFactory.Options()
 *          option.inSampleSize = 5                 ==> 5분의 1 사이즈로 줄여줌
 *
 * - 갤러리앱의 ContentProvider 가 제공하는 InputStream 객체를 획득
 * - InputStream 객체에 의해 넘어오는 데이터 이용
 *          var inputStream = contentResolver.openInputStream(it.data!!.data!!)         //InputStream 객체 획득
 *          val bitmap = BitmapFactory.decodeStream(inputStream, null, option)          // 여기에 로딩해서 이미지 객체를 만들어 준다.
 *
 */