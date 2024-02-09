package com.tak.c77

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}


/**
 * part 21. Background Limit 문제를 해결해 보자.
 * 76강 : Concepts of Background Limit
 *
 * * 백그라운드 제약 문제.
 *
 * <Background Limit>
 * - 안드로이드 앱은 액티비티, 서비스, 브로드캐스트 리시버, 컨텐츠 프로바이더 로 구성 (Activity, Service, BroadcastReceiver, ContentProvider)
 * - 액티비티를 제외하고 화면과 상관없는 백그라운드 작업을 목적
 *
 * * 액티비티 : UI
 * * Service, BroadcastReceiver, ContentProvider : 백그라운드 작업
 *
 * * 백그라운드 제약은 Service, BroadcastReceiver 이 두 부분에 제약이 가해진 것.
 * * ex) 화면이 켜지지 않아도 서비스만 하루종일 켜져서 무언가를 굴린다거나, boot complete 된 다음, Receiver 를 구동시키고 얘가 서비스를 구동시켜서 뭔가를 계속 굴리는
 *      그런게 가능했었는데 제약에 의해서 막힘
 *      => 아마도 안드로이드 시스템의 어떤 퍼포먼스적인 문제일 수도 있을 것 같음
 *      => 브로드캐스트 리시버 같은 경우에는 인텐트만 발생하면 한꺼번에 다 실행 돼 버리는데, (없으면 말고, 있으면 싹 다 실행하고) 필요없는 것 까지 실행할 가능성이 있다는 거지
 *
 * - 서비스, 브로드캐스트 리시버를 이용하여 앱의 화면이 출력되지 않은 상황에서도 백그라운드 업무를 처리하기 가능 (이전에는 가능했으나,.....)
 * - 안드로이드 8 버전 (API Level 26 버전) 부터 백그라운드 제약 (이때부터 불가해졌다.)
 *
 *
 * - 이전에 백그라운드에서 처리가 가능했던 것들이 불가능해지기 시작
 * - 시스템이 부팅이 완료되자 마자 브로드캐스트 리시버를 실행 가능
 * - 이 상황에서 서비스를 실행시켜 어떤 업무를 처리하는 것이 안드로이드 8 버전부터 불가능
 *
 *
 */


/**
 * 77강 : Broadcast Limit
 * [브로드캐스트 제약]
 *
 * * 백그라운드 제약은 크게 브로드캐스트 제약과 서비스 제약으로 나뉜다.
 *
 *
 * <Broadcast Limit>
 * - 브로드캐스트 리시버를 실행시키기 위해서는 인텐트를 sendBroadcast() 함수로 발생시킨다.
 * - 브로드캐스트 리시버를 암시적 인텐트에 의해 실행 시키는 것 금지.
 */