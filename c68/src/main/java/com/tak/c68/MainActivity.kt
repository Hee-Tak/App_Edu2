package com.tak.c68

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultView = findViewById<TextView>(R.id.resultView)
        val button = findViewById<Button>(R.id.button)

        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply {
            var isCharging = "Not Plugged"
            when(getIntExtra(BatteryManager.EXTRA_STATUS, -1)){
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    when(getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)){
                        BatteryManager.BATTERY_PLUGGED_USB -> {
                            isCharging = "USB Plugged"
                        }

                        BatteryManager.BATTERY_PLUGGED_AC -> {
                            isCharging = "AC Plugged"
                        }
                    }
                }
            }

            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100

            resultView.text = "$isCharging, $batteryPct %"
        }

        button.setOnClickListener {
            val intent = Intent(this, MyReceiver::class.java)
            sendBroadcast(intent)
        }
    }

    // 에뮬레이터에서 배터리 조작하는거 확인하려면 에뮬레이터 메뉴에서 ...(more) -> Battery 에서 변경
}

/**
 * 파트18 - BroadcastReceiver 를 이해해 보자.
 * 67강 : Concepts of BroadcastReceiver
 *
 * 안드로이드 4개 컴포넌트 중에 하나인 BroadcastReceiver   => api 적으로는 가장 간단한 컴포넌트
 * 지금까지 살펴본 컴포넌트는 화면 출력 목적으로 하는 Activity Component 였음
 *
 *
 * <BroadcastReceiver>
 * - 이벤트 모델로 실행되는 컴포넌트      => 유저 이벤트를 얘기하지는 않음. 시스템의 특정 상황을 말하는 것. 결국 시스템에 의해서 실행되는 컴포넌트
 *                               => 브로드캐스트 리시버도 컴포넌트라서 개발자가 만드는 클래스이지만, 개발자 코드에 의해서 직접 생성해서 라이프사이클을 관리할 수는 없다.
 *                               => 직접 만들거나, 만들어져있는 리시버를 실행시키는 경우도 많다.
 * - BroadcastReceiver 를 상속받은 클래스 작성
 *              class MyReceiver : BroadcastReceiver() {
 *                  override fun onReceive(context: Context, intent: Intent) {
 *
 *                  }
 *              }
 *
 * - AndroidManifest.xml 파일에 등록 (정적 등록)           => 컴포넌트니까 시스템에 등록해줘야 한다.
 *                                                      => Activity 는 Activity 로, 컨텐트 프로바이더는 provider 태그로.
 *              <receiver
 *                  android:name=".MyReceiver"                      //생략 불가능한 속성
 *                  android:enabled="true"
 *                  android:exported="true"></receiver>
 *
 *
 *
 *
 * * 다른 컴포넌트와 다르게 브로드캐스트 리시버는 코드에서 동적으로 등록이 가능하다.
 * - AndroidManifest.xml 에 등록시키지 않고 코드에서 필요한 순간 동적 등록이 가능 (동적 등록)
 *              val receiver = object : BroadcastReceiver(){
 *                  override fun onReceive(context: Context?, intent: Intent?) {
 *
 *                  }
 *              }       //브로드캐스트 리시버 객체 생성
 *
 *
 *              val filter = IntentFilter("ACTION_RECEIVER") //시스템에 등록이 될 때, 이 리시버가 언제 실행되어야 하는지에 대한 정보를 만들어내는 것 : IntentFilter
 *              registerReceiver(receiver, filter)          //필요한 순간에 이렇게 registerReceiver 함수로 브로드캐스트 리시버를 등록한다. //그리고 위의 필터 정보를 registerReceiver 함수에 주는 것.
 *
 *              //결국 Manifest 파일에 등록하는 것과 동일한 것.
 *
 * * 브로드캐스트 리시버를 명시적 인텐트에 의해서 실행시키겠다고 하면 클래스명 정보(name) 만 주면 되고
 *                      암시적 인텐트에 의해서 실행시키겠다고 하면 태그 사이에, IntentFilter 정보를 주면 된다. <intentfilter....<action ....
 *
 *
 * - unregisterReceiver() 함수로 등록 해제
 *              unregisterReceiver(receiver)
 *
 * =============================================================================
 * 여기서부터는 브로드캐스트를 실행 시키는 코드
 *
 * * 브로드캐스트 리시버는 컴포넌트. 액티비티와 마찬가지로 인텐트에 의해서 실행이 된다.
 * - sendBroadcast() 함수로 리시버 실행
 *              val intent = Intent(this, MyReceiver::class.java)
 *              sendBroadcast(intent)   //인텐트를 시스템에 발생시키는 함수
 *
 *              //클래스 명만 등록되어있다고 하면 명시적 인텐트, 인텐트 필터가 붙어있다면 암시적 인텐트를 준비해주면 됨.
 *
 *
 * * 컴포넌트를 실행시키기 위해서 시스템에 인텐트를 발생시켰을 때, 이 시스템에서 인텐트에 의해서 컴포넌트를 어떻게 실생시키냐? 라는 내부 메커니즘이 아래의 얘기.
 * - 액티비티 인텐트의 작동 원리
 *
 *      -----액티비티 인텐트 발생---> () ------> X          => 에러 (인텐트를 발생시킨 쪽 코드 에러)
 *                                    ------> 1개        => 실행
 *                                    ------> 여러 개     => 유저 선택으로 한 개 실행 (폰에서 뭐 실행할때 뭐뭐뭐 중에서 항상 한번만...실행할 도구를... 어쩌구 뜨던거 그거네 이 다이얼로그가 시스템 다이얼로그)
 *
 * - 브로드캐스트 리시버 동작 원리
 *
 *     -----리시버 인텐트 발생---> ()   ------> X          => 에러 발생하지 않음 (아무 일도 발생하지 않음)
 *                                   ------> 1개        => 실행
 *                                   ------> 여러 개     => 모두 실행
 *
 *
 *
 * * 여기까지가 개념 정리
 * =====================================================================================================================
 * 68강) 시스템 이벤트 활용
 * * 브로드캐스트 리시버가 실행되는 조건 중에서, 개발자 코드 임의의 순간에 실행시키는 경우도 있지만
 *                                        시스템에 특정 상황이 발생했을 때, 그 상황에 의해서 실행이 되는 경우도 꽤 많다
 *
 *  <부팅 완료>
 * - 부팅 완료 시점에 특정 업무를 진행
 * - 브로드캐스트 리시버를 만들고 AndroidManifest.xml 파일에 시스템에서 띄우는 인텐트 정보로 intent-filter 를 구성해 등록
 *
 *
 *              <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>     //퍼미션 등록
 *
 *
 *              <receiver
 *                  android:name=".MyReceiver"
 *                  android:enabled="true"
 *                  android:exported="true">
 *                  <intent-filter>                                                             //암시적 인텐트 준비, 어플리케이션 외부에서 실행시키는 거니까 인텐트 필터도. 준비
 *                      <action android:name="android:intent.action.BOOT_COMPLETED"/>
 *                  </intent-filter>
 *              </receiver>
 *
 *
 * <스크린 온/오프>   //스크린 오프 : 화면 꺼졌다는 얘기. 스크린오프가 되면 액티비티가 계속 움직일 필요가 없는 그런 상황이 있을 수 있다는 얘기, 이후 다시 켜지면 다시 업무 처리하는 것까지
 * - 스크린이 온 혹은 오프되는 순간 브로드캐스트 리시버를 실행
 * - 코드에서 registerReceiver() 함수를 이용해 동적 등록해야만 실행
 *
 *              val filter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
 *                  addAction(Intent.ACTION_SCREEN_OFF)
 *              }
 *              registerReceiver(receiver, filter)
 *
 * <배터리 상태>
 * - 배터리 상태와 관련된 액션 문자열
 *              android:intent.action.BATTERY_LOW                   : 배터리가 낮은 상태로 변경되는 순간
 *              android:intent.action.BATTERY_OKAY                  : 배터리가 정상 상태로 변경되는 순간
 *              android:intent.action.BATTERY_CHANGED               : 충전 상태가 변경되는 순간
 *              android:intent.action.ACTION_POWER_CONNECTED        : 전원이 공급되기 시작한 순간
 *              android:intent.action.ACTION_POWER_DISCONNECTED     : 전원 공급을 끊은 순간
 */