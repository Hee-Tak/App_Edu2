package com.tak.c55

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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