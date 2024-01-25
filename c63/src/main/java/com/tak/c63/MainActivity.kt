package com.tak.c63

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * 구글의 다양한 앱을 연동해 보자
 * Concepts of ContentProvider
 */

/**
 * 62강 : Concepts of ContentProvider
 *
 * 구글에서 기본적으로 제공되는 앱 : 대표적인 것이 주소록 앱, 갤러리 앱 등
 * 이것들을 활용하기 위해서는 ContentProvider 라는 컴포넌트에 대해서 정리가 되어야 한다.
 *
 * - 컨텐츠 프로바이더는 앱과 앱간의 데이터 연동을 목적으로 하는 컴포넌트
 *                          [여기서부터 내부 앱]
 * Outer App  --->  Content Provider --> data ----Activiry
 *                                            ----BroadcastReceiver
 *                                            ----Service
 *
 * - 컨텐츠 프로바이더는 ContentProvider 를 상속받아 작성
 *          class MyContentProvider : ContentProvider() {
 *
 *              override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int { }
 *
 *              override fun getType(uri: Uri): String? {  }
 *
 *              override fun insert(uri: Uri, values: ContentValues?): Uri? {  }
 *
 *              override fun onCreate(): Boolean {  }
 *
 *              override fun query(
 *                  uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?
 *                  ): Cursor? {  }
 *
 *              override fun update(
 *                  uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?
 *                  ): Int {  }
 *          }
 * * api 가 이전 시간에 살펴봤던 SQLite 의 api 와 거의 흡사하다. 근데 api만 이럴 뿐이고, 실제로 그렇게 될지는 개발하기 나름.
 * * 외부앱에서 이 함수를 호출해서 쓰는거임.
 * * 외부에서 이 함수들 중 하나를 호출하면 외부 앱에 해당 데이터를 넘겨주는 것.
 *
 *
 * - AndroidManifest.xml 파일에 등록
 *      <provider
 *          android:name=".MyContentProvider"
 *          android:authorities="com.example.provider"              -> 다른 것들은 manifest에 등록할 때, name 만 필수인게 많으나, 얘는 authorities 속성까지 필수임.
 *          android:enabled="true"
 *          android:exported="true"></provider>
 *
 *
 * - 컨텐츠 프로바이더를 이용하기 위해서 인텐트를 발생시키지 않는다.
 * - 컨텐츠 프로바이더는 필요한 순간 시스템에서 자동으로 생성 (인텐트를 발생시키지 않아도 된다는 뜻)
 * - 컨텐츠 프로바이더를 이용하고자 하는 앱은 query(), insert(), delete(), update() 함수만 호출
 *
 *
 * - 외부 앱의 컨텐츠 프로바이더를 이용하고자 한다면 해당 앱을 이용하기 위한 Query Visibility 관련 설정
 * - 컨텐츠 프로바이더를 가지고 있는 앱의 패키지명을 <package> 태그로 혹은 컨텐츠 프로바이더의 authorities 문자열을 <provider> 태그로 선언
 *      <queries>
 *          <!--        둘중 하나만 선언되어 있으면 된다. -->
 *          <!--        <provider android:authorities="com.example.test.provider"/>  -->
 *                      <package android:name="com.example.test_outter"/>
 *      </queries>
 *
 *
 *
 * - 시스템의 컨텐츠 프로바이더를 이용하기 위한 객체가 ContentResolver 객체
 * - ContentResolver 객체는 contentResolver 프로퍼티를 획득하여 query(), insert(), update(), delete() 함수를 호출
 *          contentResolver.query(
 *              Uri.parse("content://com.example.test.provider"),   --> 위의 함수들은 모든 첫 매개변수가 Uri이고. 이 Uri는 식별자임.
 *              null, null, null, null)
 *
 *
 * - 컨텐츠 프로바이더를 식별하기 위한 Uri 객체
 *          content://com.example.test.provider
 *         [scheme]         [host]
 *
 * - 프로토콜 명 : content
 * - 해당 컨텐트 프로바이더의 식별자 : com.example.test.provider
 *
 */


/**
 * C63 : Contacts App
 *
 * - 주소록 앱을 연동하여 주소록 목록 화면을 띄우기     => Activity  <-- 우리 앱에서 이 액티비티를 실행시켜야되겠네? ==> Intent
 * - 유저가 선택한 사람의 전화번호 혹은 이메일 정보를 획득  => ContentProvider (주소록 앱의)
 *          <uses-permission android:name="android.permission.READ_CONTACTS"/>      => Manifest 파일에 등록
 *
 * - 주소록의 목록 화면을 띄우기
 *          val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
 *          requestActivity.launch(intent)
 *
 * * 우리 앱 입장에서 주소록 앱은 외부 앱이다. => 그래서 암시적 인텐트 사용
 *
 *
 *
 * - 주소록 앱을 연동하기 위한 URL
 * - ContactsContract.Contacts.CONTENT_URI : 모든 사람 출력
 * - ContactsContract.CommonDataKinds.Phone.CONTENT_URI : 전화 번호 정보가 있는 사람만 출력
 * - ContactsContract.CommonDataKinds.Email.CONTENT_URI : 이메일 정보가 있는 사람만 출력
 *
 *
 * - 컨텐트 프로바이더를 이용해서 데이터를 획득해야하는데..
 *
 *      App     --Intent-->        Activity
 *              <--URL(식별자--
 *
 *
 * - 주소록에서 전달할 결과는 URL 문자열 형태이며, URL 의 맨 마지막 단어(위에서는 1144)가 유저가 선택한 사람의 식별자 값
 * - 식별자를 조건으로 주소록 앱의 컨텐츠 프로바이더 이용
 *          val cursor = contentResolver.query(
 *                              it.data!!.data!!,
 *                              arrayOf<String>(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
 *                              null,
 *                              null,
 *                              null
 *                          )
 *
 */



