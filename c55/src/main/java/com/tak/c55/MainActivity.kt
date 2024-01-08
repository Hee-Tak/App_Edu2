package com.tak.c55

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editView = findViewById<EditText>(R.id.editView)
        val checkView = findViewById<CheckBox>(R.id.checkView)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val getButton = findViewById<Button>(R.id.getButton)
        val resultView = findViewById<TextView>(R.id.resultView)

        val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        saveButton.setOnClickListener {
            sharedPref.edit().run {
                putString("data1", editView.text.toString())
                putBoolean("data2", checkView.isChecked)
                commit()
            }
        }

        getButton.setOnClickListener {
            val data1 = sharedPref.getString("data1", "none")
            val data2 = sharedPref.getBoolean("data2", false)
            resultView.text = "data1: $data1,  data2: $data2"
        }
    }
}


/**
 * 파트15 - SharedPreference 로 앱 데이터를 저장해보자
 * 54) Concepts of Persistence
 */

/**
 * 데이터 저장
 * - File Read/Write
 * - SharedPreference
 * - Database
 *
 * -> 일반적으로는 서버사이드 DB를 가장 많이 쓸 것.
 * -> 서버사이드 DB 와 백엔드 애플리케이션 <---네트워킹---> 안드로이드 어플리케이션
 * -> 이 네트워킹을 통해 백엔드 애플리케이션에서 서버사이드 DB에 저장하거나 데이터를 가져와서 전달하는 구조
 * * 여기서 안드로이드 앱의 데이터 영속화 기법은 백엔드 쪽을 얘기하는 것이 아님. 이건 안드로이드 앱 입장에서 네트워킹 프로그램
 *                          (->로컬)
 *                              Stand alone 하게 데이터를 저장하는 방법을 보겠다라는 것. 한개의 어플리케이션이 가지는 최소한의 기능임
 *
 * 그래서 이제부터 stand alone 하게 로컬에 데이터를 저장하는 방법에 대해 살펴볼 것
 *
 * 데이터 저장
 * - File Read/Write    => 주로 이미지 데이터 때문에 많이 들어가긴함
 * - SharedPreference   => 그래도 얘랑 db가 쉬우니까 주로 얘랑 DB를 씀 =>문자열, 숫자값, 불린값 같은 경우
 * - Database
 *
 */

/**
 * File
 *
 * - 자바 API 로 File Read/Write
 *
 * - File : 파일 및 디렉토리를 지칭하는 클래스
 * - FileInputStream / FileOutputStream : 파일에서 바이트 스트림(=>이미지 데이터)으로 데이터를 읽거나 쓰는 클래스
 * - FileReader / FileWriter : 파일에서 문자열 스트림으로 데이터를 읽거나 쓰는 클래스
 *
 * 나중에 보겠지만 SharedPreference 도 결국에는 파일로도 저장이 된다.
 *
 *
 * - 파일이 저장되는 곳을 내장 메모리 공간과 외장 메모리 공간으로 구분 (안드로이드의 특징이래)
 * - 외장 메모리 공간은 앱별 저장공간과 공유 저장 공간으로 구분. (내장 메모리 공간은 앱별 저장 공간)
 *
 * - 내장 메모리 공간이란, 앱이 설치되면 시스템에서 자동으로 할당하는 메모리 공간
 * - 앱의 패키지명(->식별자)으로 폴더를 만들어 주며 이 폴더가 이 앱의 내장 메모리 공간
 *      val file = File(filesDir, "text.txt")
 *      val writeStream: OutputStreamWriter = file.writer()
 *      writeStream.write("hello world")
 *      writeStream.flush()
 */

/**
 * SharedPreference
 *
 * - 데이터를 키-값 형태로 저장 => 그렇기 때문에 SharedPreference 를 Map 객체 정도라고 가정을 해서 이용해도 됨
 * - 내부적으로 내장 메모리의 앱 폴더에 XML 파일로 데이터가 저장 (내부적으로 데이터를 영속적으로 저장함 => 파일에다가 (xml파일))
 * - 직접 read/write 를 하는 구조가 아니라 알아서 read/write 하는 구조라고 함
 * - 이미지 데이터 저장에는 부적절함
 * - 내장 메모리 공간에 저장된다 => 해당하는 그 앱에서만 사용할 수 있다.
 *
 *
 * - Activity.getPreferences (int mode)
 * - 하나의 액티비티를 위한 데이터 저장을 목적
 * - 이 함수를 이용한 액티비티 클래스명으로 XML 파일명이 만들어 짐
 *
 *
 * - Context.getSharedPreferences (String name, int mode)
 * - 앱 전체의 데이터를 키-값 형태로 저장      => 앱 전역을 위한 데이터가 된다.
 *      val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
 *
 * - 데이터를 저장하기 위해서는 SharedPreferences.Editor 클래스의 함수를 이용
 * - SharedPreferences.Editor 객체는 SharedPreferences 의 edit() 함수로 획득
 * - commit() 하는 순간 저장
 *      putBoolean(String key, boolean value)
 *      putInt(String key, int value)
 *      putFloat(String key, float value)
 *      putLong(String key, long value)
 *      putString(String key, String value)
 *
 *
 * - 데이터 획득은 SharedPreferences 의 getter 함수를 이용
 *      getBoolean(String key, boolean defValue)
 *      getFloat(String key, float defValue)
 *      getInt(String key, int defValue)
 *      getLong(String key, long defValue)
 *      getString(String key, String defValue)
 *
 */