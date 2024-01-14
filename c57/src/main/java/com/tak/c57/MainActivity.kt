package com.tak.c57

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * PreferenceFragmentCompat
 *
 * - 설정을 위한 XML 만 만들어 적용시켜 주면 설정 화면이 나오고 유저가 설정 항목을 클릭하면 설정 내용이 자동으로 저장
 *      //여기까진 자동으로 나오기 때문에 코드에서 제어할 필요가 없음
 *
 * - 어떤 경우에는 설정 XML 의 설정 부분을 코드에서 제어 필요
 * - 설정 항목을 클릭하는 순간의 이벤트를 처리하고 싶거나 코드가 실행되어 발생한 결과를 설정 항목의 title 혹은 summary 로 지정하고 싶은 경우
 *      (개발자 코드에서 이 설정을 제어하고 싶은 경우)
 *
 *
 *
 * - 설정을 위해 사용한 태그에 해당되는 객체를 findPreference() 태그로 획득
 *      <EditTextPreference
 *          android:key="id"
 *          android:title="ID 설정"
 *          android:isPreferenceValue="false/>
 *
 *
 *      val idPreference: EditTextPreference? = findPreference("id")
 *      idPreference?.isVisible = true
 *
 *
 */