package com.tak.c66

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val dataButton = findViewById<Button>(R.id.dataButton)
        val fileButton = findViewById<Button>(R.id.fileButton)

        val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val bitmap = it.data?.getExtras()?.get("data") as Bitmap
            bitmap?.let {
                imageView.setImageBitmap(bitmap)
            }
        }

        dataButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            launcher.launch(intent)
        }

        var filePath = ""
        /*val fileLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val option = BitmapFactory.Options()
            option.inSampleSize = 3
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                imageView.setImageBitmap(bitmap)
            }
        }*/
        /*val fileLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val option = BitmapFactory.Options()
                option.inSampleSize = 3
                val bitmap = BitmapFactory.decodeFile(filePath, option)
                bitmap?.let {
                    imageView.setImageBitmap(bitmap)
                }
            } else {
                // 촬영이나 파일 선택이 취소되었을 때의 처리를 추가할 수 있습니다.
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        fileButton.setOnClickListener {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            filePath = file.absolutePath
            val uri = FileProvider.getUriForFile(
                this,
                "com.tak.android.c66.fileprovider",
                file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            fileLauncher.launch(intent)
        }


         */
        val fileLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val option = BitmapFactory.Options()
                    option.inSampleSize = 3
                    val bitmap = BitmapFactory.decodeFile(filePath, option)
                    bitmap?.let {
                        imageView.setImageBitmap(bitmap)
                    }
                } else {
                    // 촬영이나 파일 선택이 취소되었을 때의 처리를 추가할 수 있습니다.
                    Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error in fileLauncher", e)
            }
        }

        fileButton.setOnClickListener {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            filePath = file.absolutePath

            val uri = FileProvider.getUriForFile(
                this,
                "com.tak.android.c66.fileprovider",
                file
            )

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            fileLauncher.launch(intent)
        }
    }
}

/**
 * C66
 * <Camera App>
 *
 * - 카메라 앱을 연동하여 사진을 촬영하고 결과를 되돌려 받는 방법은 두가지 방법
 * - 사진 데이터 획득 방법       => 이거는 카메라앱에서 파일을 저장하지 않는다. 카톡에서 기능 써보면 감 바로 온다. => 사이즈도 작게 나옴 (OOM 문제 때문에)
 * - 파일 공유 방법
 *                      이 두가지 방법은 인텐트 정보를 틀리게 줘서 하면 된다. (다르게 인듯)
 *
 *
 *              App  ------------Intent-----------> Activity
 *                   <----------Data(FILE)---------
 *                   -----------DATA(FILE)-------->
 *
 *
 * - 사진 데이터 획득 방법
 *              val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
 *              requestActivity.launch(intent)
 *
 *              val bitmap = it.data?.getExtras()?.get("data") as Bitmap
 *
 *              이렇게 인텐트 생성하고 받아와서 비트맵 객체로 이용해주면 됨.
 *
 * - 파일 공유 방법
 *      > 앱에서 사진을 저장할 파일을 만든다.
 *      > 파일 정보를 포함해서 인텐트를 발생시켜 카메라 앱을 실행시킨다.
 *      > 카메라 앱에서 사진 촬영 후 촬영된 사진을 공유된 파일에 저장을 한다.
 *      > 카메라 앱이 종료되면서 성공 실패를 반환한다.
 *      > 앱에서 파일을 읽어 카메라 앱이 저장한 사진 데이터를 이용한다.
 *
 *      * 어플리케이선과 어플리케이션 간의 파일 공유 문제 때문에 그냥은 안됨.
 *
 * - FileProvider 를 이용하려면 공유하고자 하는 파일의 Uri 값을 준비. => res/xml 아래에 xml 파일 만들기
 *              <paths xmlns:android="http://schemas.android.com/apk/res/android">
 *                  <external-path name="myfiles" path="Android/data/com.tak.android.c66/files/Pictures"/>
 *              </paths>
 *
 * - AndroidManifest.xml 파일에 등록 (위의 xml 파일을.)
 *              <provider
 *                  android:name="androidx.core.content.FileProvider"           => 이거를 쓸건데
 *                  android:authorities="com.tak.android.c66.fileprovider"
 *                  android:exported="false"
 *                  android:grantUriPermissions="true">
 *                  <meta-data
 *                      android:name="android.support.FILE_PROVIDER_PATHS"
 *                      android:resource="@xml/file_paths></meta-data>          => 이 file_paths 를 지정해주는게 중요
 *              </provider>
 *
 * - 카메라 앱을 연동하기 위해서 파일 생성
 *              val file = File.createTempFile(
 *                              "JPEG_${timeStamp}_",
 *                              ".jpg",
 *                              storageDir
 *                          )
 *              filePath = file.absolutePath
 *
 *
 * - 파일을 공유하기 위한 Uri 객체를 만들고, 이 정보를 인텐트의 엑스트라 데이터로 설정
 *              val photoURI: Uri = FileProvider.getUriForFile(
 *                                      this,
 *                                      "com.tak.android.c66.provider", file
 *                                  )
 *              val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
 *              intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);             <= 여기에 파일 정보가 들어간다는 얘기
 *              fileRequestActivity.launch(intent)                      //이런식으로 인텐트를 발생시키면 파일 정보가 카메라 앱에 까지 전달이 된다.
 *
 *
 * - 파일 경로를 decodeFile() 함수에 지정하여 Bitmap 객체를 획득
 *              val bitmap = BitmapFactory.decodeFile(filePath, option)  //이미 아는 파일 경로에서, 데이터를 뽑아서 이미지 객체로 만들어주면 된다. 라는 얘기
 *
 *
 *
 *
 *
 *
 *
 */