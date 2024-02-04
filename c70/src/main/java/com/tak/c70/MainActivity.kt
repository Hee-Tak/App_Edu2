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


/**
 *  70강 : Service Lifecycle
 *
 * * 안드로이드 컴포넌트 4개 중, 액티비티와 서비스는 라이프사이클을 정리를 해줘야 한다.
 *      브로드캐스트 리시버와 컨텐트 프로바이더 같은 경우에는 개발자가 라이프 사이클을 신경을 안써도 상관은 없다. (시스템에 의해 동작)
 *
 *
 *  <Service Lifecycle>
 * - 서비스를 실행시키는 방법이 startService() 함수와 bindService() 함수 두 개가 있음. (인텐트 발생)
 * - 어느 함수를 이용해 서비스가 실행되는지에 따라 서비스의 라이프 사이클이 다름.
 * - 서비스 클래스는 싱글톤으로 동작 (싱글톤 : 하나의 클래스가 동작하기 위해서는 객체가 생성이 되는데, 단 하나의 객체로만 동작하는 구조)
 *
 *      ---인텐트--->액티비티      => 이 인텐트 정보를 해석해서 시스템에서 이 액티비티를 실행시키는 것
 *                              => 그러면 당연하지만 이 액티비티 객체가 생성이 돼서 동작을 함.
 *
 *                              이러한 상황에서 똑같은 액티비티를 실행시키기 위핸 인텐트가 한 번 더 발생이 되면??
 *                              액티비티는 싱글톤으로 동작하지 않는다. 그렇기때문에 똑같은 액티비티를 실행시키기 위한 인텐트가 더 발생이 된다고 한다면,
 *                              또 하나의 객체가 생성이 된다.
 *                              그러니까 인텐트만 생성이 되면, 액티비티 객체는 다수 개가 생성될 수 있는 구조.
 *                              왜 이렇게 동작하나? -> 액티비티가 화면을 목적으로 하기 때문.
 *
 * * 서비스는 좀 다름.
 *                  서비스 클래스가 있고, 인텐트가 발생이 돼서 이 서비스가 동작을 하는 것 인데.
 *                  당연하지만 객체는 생성이 돼야 한다.
 *                  인텐트가 한번 더 발생한다고 해서, 객체를 다시 생성하지 않는다.
 *                  이전에 생성되던 객체를 그대로 이용하는 것.
 *                  즉, 몇번이고 인텐트가 발생한다고 해도, 서비스는 싱글톤으로 움직인다는 것.
 *                  => 이렇게 할 수 있는 이유 -> 서비스가 화면 출력이 되지 않다 보니까, 매번 요청시에 다른 업무가 다른 데이터로 진행이 된다고 하더라도 얘 내부적인 알고리즘으로 해결할 수가 있기 때문.
 *
 *
 *                                           <startService() Lifecycle>
 *
 *                                            Call to startService()        //최초의 객체가 생성
 *                                                      |
 *                                                      |
 *                                                      ↓
 *                                                  onCreate()              //서비스 객체 생성시에 최초의 한 번만 호출되는 함수
 *                                                      |
 *                                                      |
 *                                                      |
 *                                          ------------------------
 *                                                      |
 *                                                      ↓
 *                                             onStartCommand()             //인텐트가 다시 or 더 생성되면 onStartCommand() 만 다시 호출이 된다.
 *                                                      |                   //즉, 얘는 반복호출 가능성이 있다. //그렇다는 얘기는 당연히 이걸 고려해서 코딩해야하고, 아마 주로 여기 위에서 코딩하게 될 것.
 *                                                      |
 *                                                      ↓
 *                                              Service running             //서비스 구동 상태 - 백그라운드 업무 장기간동안 진행
 *                                                      |
 *                                                      |   The service is stopped by itself or a client (call to stopService())
 *                                                      |
 *                                          ----------------------
 *                                                      |
 *                                                      ↓
 *                                                 onDestroy()
 *                                                      |
 *                                                      ↓
 *                                              Service shut down
 *
 *                                               unbounded service
 *
 *
 *
 *
 *
 *                                           <bindService() Lifecycle>              * bind : 두 개의 정보를 서로 연결하는 작업, 결박하다, 포박하다
 *
 *                                            Call to bindService()             //서비스 객체 생성
 *                                                      |
 *                                                      |
 *                                                      ↓
 *                                                  onCreate()                  // 최초 1회만 호출
 *                                                      |
 *                                                      |
 *                                                      |
 *                                          ------------------------
 *                                                      |
 *                                                      ↓
 *                                                  onBind()                    //반복호출 가능 (인텐트가 여러개 또는 다시 생성됨으로 인해)
 *                                                      |
 *                                                      |
 *                                                      ↓
 *                                         Clients are bound to service         //서비스 구동 상태
 *                                                      |
 *                                                      |
 *                                                      |    All clients unbind by calling unbindService()
 *                                                      ↓
 *                                                  onUnbind()                  //서비스 종료시키기 위한 호출       //마찬가지로 얘도 요청단위로 반복호출 가능 (당연하겠지만 onBind() 가 호출되는 만큼 이라는 뜻)
 *                                                      |
 *                                                      ↓
 *                                          ----------------------
 *                                                      |
 *                                                      ↓
 *                                                 onDestroy()
 *                                                      |
 *                                                      ↓
 *                                              Service shut down
 *
 *                                               Bounded service
 *
 *
 *
 */