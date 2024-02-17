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
 *
 * * 브로드캐스트 리시버에 제약이 가해졌다 하더라도, 우리 개발 코드에 전혀 문제가 발생하지 않을 것으로 보임.
 * * 다른 방법으로 얼마든지 실행시킬 수가 있다.
 *
 *          <receiver
 *              android:name=".MyReceiver"
 *              android:enabled="true"
 *              android:exported="true">
 *              <intent-filter>                                 //이렇게 돼 있더라도 ㄱㅊ. 내부에서는 위의 클래스명 정보로 실행(명시적), 외부에서는 인텐트필터 정보로 실행(암시적)
 *                  <action android:name="ACTION_RECEIVER/>
 *              </intent-filter>                                //인텐트 필터를 등록했다는 것은, 암시적으로 브로드캐스트 리시버가 실행이 가능하게 설정한 것.
 *          </receiver>
 *
 *
 *          val intent = Intent("ACTION_RECEIVER")          //<-- 클래스명 정보를 준게 아니니까 암시적 인텐트
 *          sendBroadcast(intent)                           //백그라운드 제약때문에 암식적으로 실행이 불가.
 *
 * * 여기서, 암시적으로 실행한게 문제가 되지 않는 이유
 *      -> 위의 코드들을 하나의 App 으로 보고, 그리고 다른 App 이 있다고 생각하자.
 *      -> App 내에 다른 어떤 액티비티가 있고, 이 액티비티가 암시적 인텐트로 인텐트 발생시켜서 할 경우에는 브로드캐스트 리시버가 실행이 안됨.
 *      -> 그런데, 외부 App 에서, 외부 앱에 의해서 인텐트 발생인데, 이 인텐트가 암시적 인텐트면, 이건 잘 실행이 됨.
 *      -> 그럴 수 밖에 없는게, 외부 앱에서 만약에 내부 App 쪽에 브로드캐스트 리시버를 실행시키겠다고 하면 명시적으로 할 수가 없기 때문.
 *      -> 그래서 인텐트 필터 등록하고, 인텐트 필터 정보를 맞춰서 하는 것.
 *
 *      => 결론 : 암시적 인텐트에 의해서 실행이 안된다 라고 하는 것은, 내부에서 암시적 인텐트에 의해서 실행이 못되게끔 막은 것을 말하는 것.
 *      => 그리고, 내부에서는 클래스명 정보가 있기 때문에 ㄱㅊㄱㅊ 명시적으로 가면 됨.
 *
 *
 *
 * - 암시적 인텐트를 발생시키면 브로드캐스트 리시버는 실행되지 않고 에러가 발생 (내부에서 암시적 인텐트를 발생시킬 때의 이야기)
 *
 *          Background execution not allowed: receiving Intent { act=ACTION_RECEIVER flg=0x10 } to com.example.test
 *
 *
 *
 * - AndroidManifest.xml 파일에 등록된 리시버를 암시적 인텐트로 실행시키는 경우 금지
 * - 명시적으로 실행시키는 것은 가능
 * - 코드에서 registerReceiver() 로 등록시킨 경우에는 암시적 인텐트에 실행 가능 (동적 등록으로 암시적 인텐트를 실행할 경우에는 가능)
 *
 *          receiver = object : BroadcastReceiver() {
 *              override fun onReceive(context: Context?, intent: Intent?) {
 *                  Log.d("tak", "outer app dynamic receiver..........")
 *              }
 *          }
 *
 *          registerReceiver(receiver, IntentFilter("ACTION_OUTER_DYNAMIC_RECEIVER"))       //receiver 객체를 등록한거니까. 인텐트 필터 정보를 줄 수 밖에 없고, 암시적 인텐트로 발생시키는 것.
 *
 *
 */

/**
 * 78강 : Service Limit
 *
 * <Service Limit>
 *                                                                                  (브로드캐스트와는 다르게)
 * - 앱이 백그라운드 상태에 있을 때 서비스를 실행시키기 위해서 인텐트를 발생시키면 에러 발생 => 명시적 인텐트, 암시적 인텐트 모두 다 에러.
 * - 앱이 포그라운드 상황이라면 정상 실행
 *
 *          Not allowed to start service Intent { act=ACTION_OUTER_SERVICE pkg=com.example.test }: app is in background uid null
 *
 *
 *
 *
 * * 꼭 대놓고 포그라운드 상황만 보는게아니라, 몇가지 경우를 포그라운드 상황으로 봐주는 경우가 있다.
 *
 * - Activity 가 시작되거나 일시 중지되거나 상관없이 보이는 Activity 가 있는 경우 (포그라운드 상황)
 *
 * - 포그라운드 서비스가 있는 경우 (즉, 여러 개의 서비스 중에 포그라운드 서비스가 존재할 경우. 다른것도 구동될 수 있다는 걸 뜻함)
 * - 앱의 서비스 중 하나에 바인드하거나, 앱의 콘텐츠 프로바이더 중 하나를 사용하여 앱에 또 다른 포그라운드 앱이 연결된 경우
 *      (다른 포그라운드 앱과 연결되어 사용되는 경우) (이 연결은 앱의 콘텐츠 프로바이더 중 하나를 사용하여 연결)
 *      다른 앱이, 우리의 서비스를 bindService 방식으로 이용하고 있다면
 *      우리 앱은 백그라운드긴 하지만, 우리 앱과 연동하고 있는 다른 쪽에 앱이 포그라운드 상황일 수 있는 것.
 *      우리 앱과 연동하면서 데이터를 그쪽에 뿌릴 수 있어서, 결국 우리 데이터가 화면에 나올 수가 있다는 것. 이럴 때는 포그라운드 상황으로 봐줌.
 *
 *      혹은 컨텐트 프로바이더도 마찬가지.
 *      컨텐트 프로바이더를 다른 쪽에서 이용하고 있다면, 우리 앱은 백그라운드 상황이지만, 다른 쪽 앱에서 우리 앱의 데이터가 뿌려질 수가 있어서
 *      포그라운드 상황으로 보고, 이런 경우에는 서비스가 동작이 된다.
 *
 *
 *
 *
 * - 앱이 백그라운드 상황에 있다고 하더라도 아래의 경우에는 서비스가 정상적으로 실행
 *
 * - 우선순위가 높은 Firebase 클라우드 메시징 (FCM) 메시지 처리
 * - SMS/MMS 메시지와 같은 브로드캐스트 수신
 * - 알림에서 PendingIntent 실행 (Notification) => 특히 이 알림에서는 우리 앱의 정보가 뿌려질 수 있으니 그런듯.
 * - VPN 앱이 포그라운드로 승격되기 전에 VpnService 시작
 *
 *
 *
 *
 *
 * - 백그라운드 상황에서 서비스가 정상 실행되게 할 수 있는 방법
 * - startForegroundService() 함수에 의해 인텐트를 발생시키면 앱이 백그라운드 상황이라고 하더라도 정상적으로 서비스가 실행
 *
 *          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 *              startForegroundService(intent)
 *          } else {
 *              startService(intent)
 *          }
 *
 * * 이것과 관련된 가장 쉬운 테스트
 *      => 폰에 boot complete 시에, 우리의 리시버가 실행되게 만들고,
 *          그런 다음에, 브로드캐스트 리시버가 서비스를 구동시켜 버리면
 *          그러면 부팅이 완료되자마자 서비스가 구동되는 거니까, 이 앱은 완벽하게 백그라운드 상황이다.
 *          그 상황에서 브로드캐스트 리시버가 서비스를 실행시켜서 인텐트를 발생시키면 실행이 안된다는 것.
 *
 *          그런데 브로드캐스트 리시버가 서비스를 실행시키면서 startForegroundService 를 가지고 실행을 시켰다고 하면
 *          정상적으로 실행이 된다.
 *
 *          근데 이 함수에는 조건이 붙음. 일단은 실행이 되기는 되는데
 *          아주 빠른 시간 내에 뭔가를 띄워줘야함. 그러지 않으면 에러가 발생한다.
 *
 * - startForegroundService() 함수에 의해 서비스가 정상 실행되기는 하지만 얼마 후 에러가 발생
 *
 *          Context.startForegroundService() did not then call Service.startForeground()
 *          => 뭔가를 call 하지 않았다.
 *
 * * startForeground() 에서 () 안에 들어갈 매개변수는 Notification.
 *
 * - startForegroundService() 에 의해 실행된 서비스 내에서 빠른 시간내에 startForeground() 함수를 호출해 줘야 한다.
 * - startForeground() 함수의 매개변수는 알림 객체 (Notification)
 *
 *          val notification = builder.build()
 *          startForeground(1, notification)            //이 경우에는 서비스가 구동이 되자마자 빠르게 notification을 띄워줘야한다는 얘기
 *
 *          <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
 *
 */


/**
 * 79강 JobScheduler
 *
 * * 안드로이드 시스템에서 백그라운드 제약이 가해지기 시작하면서, 어플리케이션이 백그라운드 상황에 있을 때, 업무 처리가 필요한 경우도 분명히 있을 것.
 *      이런 경우에 JobScheduler 를 이용할 수도 있다. (모든 경우x. 특정 조건에 따라O) (특정 조건에 따라, 백그라운드 업무 처리를 위한 클래스 JobScheduler)
 *
 *
 * <JobScheduler>
 *
 * * JobScheduler 도 일단은 서비스이다. (작성 방법에 있어서 일반적인 다른 서비스와는 차이가 있지만 어쨌던 서비스) (매니페스트 파일에 등록시켜줘야함)
 * * 일반적인 Service 같은 경우에는, 어디에선가 구동을 시키려면 인텐트를 발생시켜줘야 하는데
 * * JobScheduler 는 시스템에 등록을 하면, 시스템에서 특정상황이 됐을 때 실행시켜준다는 개념.
 *
 * - JobScheduler 을 이용하면 앱이 백그라운드 상황에서도 업무처리 가능
 * - 조건을 명시하고 그 조건에 맞는 경우에만 백그라운드 업무 처리 가능
 *      * 네트워크 타입
 *      * 배터리 충전 상태
 *      * 특정 앱의 콘텐츠 프로바이더의 갱신
 *
 *      * 실행 주기         (한시간에 한번씩 같은 식으로)
 *      * 최소 지연 시간      (delay)                         => 이러한 조건들에 충족하면, 시스템에 의해서 JobScheduler 가 실행이 된다.
 *
 *
 * <JobService>
 *
 * - 개발자 서비스 (개발자가 만드는 서비스) (서비스로 등록해야 함)
 * - android.permission.BIND_JOB_SERVICE 퍼미션이 등록되어야 함
 *
 *              <service
 *                  android:name=".MyService"
 *                  android:enabled="true"
 *                  android:exported="true"
 *                  android:permission="android.permission.BIND_JOB_SERVICE"></service>
 *
 *
 *              @TargetApi(Build.VERSION_CODES.LOLLIPOP)
 *              class MyService : JobService() {            //JobServcie 를 상속받는 Service (일반 서비스가 아니라. 잡서비스를 상속받음)
 *                                                          //onStartCommand 같은 경우에는 override 해서 작성은 가능하나. 호출이 안되기 때문에 의미가 없음.
 *                  override fun onCreate() {
 *                      super.onCreate()
 *                  }
 *
 *                  override fun onDestroy() {
 *                      super.onDestroy()
 *                  }
 *
 *                  //아래의 두 함수가 중요. 시스템에 의해서 구동이 될 때 이 함수들이 자동으로 콜이 된다.
 *                  override fun onStartJob(params: JobParameters?): Boolean { //일반적으로 백그라운드에서 진행돼야 되는 업무로직은 onStartJob에서 구현할 것.
 *                      return false                                            //return 값이 true 냐 false 냐에 따라 동작이 달라진다.
 *                  }
 *
 *                  override fun onStopJob(params: JobParameters?): Boolean {
 *                      return false
 *                  }
 *
 *              }
 *
 * - onStartJob() 이 백그라운드에서 처리되어야 하는 업무를 가지는 함수
 * - 리턴값에 따라 다르게 동작
 * - false : Job 이 완벽하게 종료되었다는 의미   //이를 시스템에 알려주는 것임. //이 경우에는 onStopJob() 이 호출되지 않겠지. (완벽하게 Job이 끝났다는 의미이기 때문에 더이상 처리할 게 없다 보니까 호출이 안되는 것)
 * - true : Job 이 아직 끝나지 않았음을 의미    //이 경우에는 종료되긴 했으나, 아직 완벽하게 끝나지 않았다는 의미 => onStopJob() 을 call 해줌. => 마지막에 뭔가 처리해야 될 것을 처리해 달라는 의미가 된다.
 *                                          //true는 보통, 내가 실행되어야 될 어떤 조건이 갑자기 바뀌었다, 이 JobService를 등록시킨 조건이 갑자기 변경이 됐다. 그래서 종료가 된다. 이럴 경우, 나는 아직 처리가 안된것아니냐. => true
 *                                          //혹은 어디선가 cancel 시켜버렸다. 아직 처리가 안된 상태지 않느냐 => true . 이런식이다.
 *
 *
 * - onStopJob() 의 리턴값도 true, false 에 따라 다르게 동작
 * - false : JobScheduler 등록이 취소        //이 onStopJob 까지 호출이 됐다만, 이 JobServcie 를 실행시킨 조건을 JobScheduler 에게 등록을 취소하는 것. 더 이상 실현될 필요 없다. 이런 의미
 * - true : 다시 JobScheduler 등록          // 다시 등록한다.
 *
 *
 *
 * * 그러면, JobScheduler 를 만든 다음에, 시스템에 등록시켜야 되는데 등록시키는 조건은?
 * * 그 조건을 담는 게 JobInfo 다.
 *
 *
 * <JobInfo>
 *
 * - 실행되는 조건을 JobInfo 객체에 담아 시스템에 등록
 *
 *              JobInfo.Builder(1, ComponentName(this, MyService::class.java)).run {
 *                  setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)  //=> 이러한 setter 함수로 조건을 주는것. 이 예제에서는 네트워크 타입 정보를 줬음. UNMETERED -> 와이파이 정보 (핸드폰이 와이파이에 붙는 순간, 실행시켜 달라는 조건이라 보면 됨)
 *                  jobScheduler?.schedule(build())                 // 이렇게 조건을 JobInfo로 준 다음에, JobScheduler 객체에 schedule 라는 함수로 등록하면, 우리가 만들어놓은 이 JobService 가 특정 조건이 됐을 때 실행된다는 구조.
 *              }
 *
 *              //그렇담 여기서는, 핸드폰이 와이파이에 붙는 순간, 이 JobService 는 실행되게 됨.
 *
 *
 * * 여기에다가 뭔가 실행돼야 될 조건을 등록해서 시스템에 알려주는 것.
 * * 그래서 JobInfo 를 빌더 객체가 만드는 것
 * * 여기에 들어가야되는 정보는? => 어떤 JobService 냐? => 이 JobService(MyService) 를 실행시키는 거다. 당연히 JobService 에 대한 정보는 들어가줘야 하는 것.
 * * 이 JobService 를 시스템에서 실행시켜준다는 것.
 *
 *
 *
 * * setter 함수들.
 *
 * - setPersisted (true)                                : 단말을 재부팅 해도 Job 등록 유지해야 하는지 설정. (원래는 재부팅하면 다시 등록해줘야 함.)
 * - setPeriodic (long intervalMillis)                  : Job 의 실행 주기 설정.
 * - setMinimumLatency (long minLatencyMillis)          : Job 의 실행의 지연 시간 설정.
 * - setOverrideDeadline (long maxExecutionDelayMillis) : 다른 조건이 만족하지 않는다고 하더라도 이 시간 안에 Job 이 실행되어야 함을 설정. (조건이 맞지 않더라도 최소 한시간에 한번은 설정되어야한다. 이런 느낌)
 * - setRequiredNetworkType (int networkType)           : 네트웍 타입 설정.                (폰이 이동사망에 붙어있는가, 와이파이에 붙어있는가)
 * - setRequiresBatteryNotLow (boolean batteryNotLow)   : 배터리가 낮은 상태가 아님을 설정.
 * - setRequiresCharging (boolean requiresCharging)     : 배터리가 충전 상태인지를 설정.
 *
 *
 */