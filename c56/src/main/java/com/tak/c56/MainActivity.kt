package com.tak.c56

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}


/**
 * Settings XML 을 만드는 방법
 */

/**
 * settings xml
 *
 * - 앱의 환경설정 자동화
 *
 * - 앱 설정을 위한 XML 작성
 * - API Level 29 이전 버전에서는 PreferenceFragment 를 이용
 * - API Level 29 버전부터는 AndroidX Preference 를 이용할 것을 권장
 *          implementation 'androidx.preference:preference-ktx:1.1.1'
 *
 * - res/xml 폴더에 설정과 관련된 XML 파일 작성 ...settings.xml
 *
 * - 루트 태그가 <PreferenceScreen>
 * - <SwitchPreferenceCompat>, <Preference> 등의 태그를 이용해 각각의 설정 항목을 준비
 *
 *          <PreferenceScreen xmlns:app="http://schemas.android.com/apk/res-auto">
 *              <SwitchPreferenceCompat
 *                  app:key="notifications"
 *                  app:title="Enable message notifications"/>
 *          </PreferenceScreen>
 *
 *          => preference (map) 형태로 저장 => key-value
 *
 *
 * - PreferenceFragmentCompat 을 상속받은 Fragment 로 설정 XML 적용
 *
 *          class SettingsFragment : PreferenceFragmentCompat() {
 *              override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
 *                  setPreferencesFromResource(R.xml.settings, rootKey)
 *              }
 *          } //=> ui가 나오는 것에 데이터 저장까지 자동으로 된다.
 *
 *
 *
 * - PreferenceScreen : 설정 화면 단위. 중첩 가능하며 중첩된 내용은 별도의 화면에 나옴.
 * - PreferenceCategory : 설정 여러 개를 시각적으로 묶어서 표현
 * - CheckboxPreference : 체크박스가 나오는 설정
 * - EditTextPreference : 글 입력을 위한 설정
 * - ListPreference : 항목 Dialog 를 위한 설정
 * - MultiSelectListPreference : 항목 Dialog 인데 체크박스가 자동 추가되어 여러 선택 가능
 * - RingtonePreference : Ringtone 선택을 위한 설정
 * - SwitchPreference : Switch 를 이용한 설정         => 얘가 좀 쓰일듯
 *
 *
 */