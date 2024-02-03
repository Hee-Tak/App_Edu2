package com.tak.c70

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * 파트19 - Service 를 이용해 백그라운드 업무를 구현해 보자.
 *
 * 69강 : Concepts of Service
 *
 * * Servcie : 안드로이드 4개 컴포넌트 중 하나. 백그라운드 업무를 담당하기 위한 컴포넌트.
 * * Activity 만큼은 아니지만, Service 도 api 와 LifeCycle 이 있다. (Service 를 구동시키는 방법에 따라 LifeCycle이 바뀐다.)
 *
 * <Service>
 * - 백그라운드에서 오랜 시간동안 수행되는 업무를 처리하기 위한 컴포넌트
 * - 화면 출력 능력은 가지지 않는 컴포넌트
 *
 *          Activity
 *          --------
 *          |       |  -> 이 Activity 내에서 Thread 프로그램을 작성하거나, 코루틴 프로그램을 작성하면
 *          |  UI   |     Thread / Coroutine 이쪽에서도 백그라운드 업무를 담당할 수가 있긴 있다.
 *          |       |       => 이 것도 백그라운드 업무가 맞긴 맞지만, 화면 반응성이 있는 백그라운드 업무라고 봐야함
 *          --------            (+시간이 오래걸리는 업무)
 *
 *
 *           Service  : 아예 화면이 목적이 아님. 서비스가 실행이 된다고 해도 화면에 아무것도 안나옴.
 *           --------
 *           |      |  only background . 화면 반응성이 없거나, 드물게 발생하는 백그라운드 업무를 처리.
 *           |      |
 *           |      |
 *           --------
 *
 *
 * - Service 을 상속받아 작성          (코어적으로 만들겠다하면 Service를 상속받아 작성하면 됨)
 *
 *              class MyService: Service() {
 *
 *                  override fun onBind(intent: Intent): IBinder? {
 *                      return null
 *                  }
 *              }
 *
 * * 서비스도 안드로이드의 컴포넌트이기 때문에 시스템에 등록해야 함.
 * - AndroidManifest.xml 에 등록
 *
 *              <service
 *                  android:name=".MyService"               //생략불가능
 *                  android:enabled="true"
 *                  android:exported="true"></service>
 *
 *              만약에 외부에서 이 서비스를 실행시키겠다고 하면 인텐트를 발생시켜줘야된다.
 *              여기서 명시적 인텐트로 쓰겠다 => 클래스명 정보만 주면 됨.
 *              암시적 인텐트에 의해 실행되게 하겠다. => service 태그 사이에, 인텐트 필터를 구성해 줘야 함.
 *
 *
 * - startService() 에 의한 실행     (인텐트 발생할 함수) (컴포넌트이기 때문에 인텐트를 발생시켜야함)
 * - 외부 앱의 서비스라면 setPackage() 함수를 이용해 실행하고자 하는 앱의 패키지명을 명시  (패키지명 : 식별자)
 *
 *              val intent = Intent(this, MyService::class.java)
 *              startService(intent)
 *
 *
 * - 서비스를 종료시키고 싶다면 stopService() 함수로 인텐트를 발생 (4개의 컴포넌트 중 유일하게 Service만. 종료하기 위한 인텐트 함수가 있다)
 *      * 일반적으로 서비스 같은 경우에는 한 번 구동되면 시간이 오래 걸리고, 화면 반응성이 없음. 그래서 유저에 의해서 종료될 수가 없다는 것.
 *          그러다보니 코드에서 필요없는 순간도 발생할 수 있다. 이런 것들을 제어하기 위해. 더이상 업무 처리가 필요 없는 것을 중단하기 위한 stopService()
 *
 *              val intent = Intent(this, MyService::class.java)
 *              stopService(intent)
 */