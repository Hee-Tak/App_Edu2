package com.tak.c74

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}


/**
 * part 20 : Notification 으로 유저 알림을 구현해 보자.
 *
 * C73) Concepts of Notification
 *
 * <Notification>
 * - 상태바에 앱의 정보를 출력하는 것을 알림(Notification) 이라고 한다.
 *
 * * 상단바에 앱의 아이콘 같은게 출력되는 것을 말하는 듯  => 상태바 : 배터리 그림이 그려져있는 그 부분 (시스템 System 창)
 * * 앱의 화면에서 상단바(상태바)를 제외한 나머지 부분은 윈도우 라고 함.
 * * 이 윈도우 내에서, 액션 바와 컨텐츠 영역으로 구분이 되는 것.
 * * 상태바는 액티비티 영역이 아님. 액티비티가 제어할 수 없는 곳. 대신에, 시스템에 의뢰를 하는 것.
 *
 *
 * - 상태바는 시스템에 의해 관리되는 곳
 * - 앱이 직접 제어 불가
 * - 앱의 상태를 상태바에 출력해 유저에게 무언가의 상황을 알려주는 기능
 *
 * * 액티비티는 어차피 유저에게 뭔가를 알릴려면 화면에서 보여주면 끝.
 * * 즉, Notification 을 쓰는 건 주로 BroadcastReceiver, Service
 *
 *
 * - 알림은 NotificationManager 의 notify() 함수로 발생          //이 발생시키는 정보를 Notification 객체가 가지고 있음.
 * - Notification 객체는 NotificationCompat.Builder 에 의해 생성
 * - NotificationChannel 로 NotificationCompat.Builder 생성    //즉, API 26 버전 부터는, NotificationChannel 이라는 개념을 사용해서 만들어 줘야 함.
 * - NotificationCompatBuilder 가 필요한데, Builder 를 만드는 방법이 API Level 26 (Android 8) 버전부터 변경
 *
 *
 *      NotificationManager
 *              |                                                    NotificationChannel
 *              |                     create                                ↓
 *              |                    ↙-----------NotificationCompat.Bulder(  )
 *              |                   ↙
 *              |           Notification
 *              |          ↙
 *              | notify(  )
 *
 */
//===============================================================================================================================
//===============================================================================================================================
/**
 * c74) NotificationManager
 *
 * <Notification.Builder>
 * - Builder(context: Context!)                         -> API Level 26 (Android 8) 이전 버전
 * - Builder(context: Context!, channelId: String!)     -> API Level 26 (Android 8) 부터
 *
 * * 설정에서 알림 켜줘야 함.
 * * Notification 받는 거를 구분해서 유저가 조정할 수 있게끔 하겠다는 개념 => 채널 개념
 *
 *
 *
 * <NotificationChannel>
 *
 * - NotificationChannel(id: String!, name: CharSequence!, importance: Int)
 *
 * - NotificationManager.IMPORTANCE_HIGH    -> 긴급 상황이며, 알림음이 울리며, 헤드업으로 표시    *헤드업? -> 배너 비슷하게 뜨는걸 얘기하는 것 같음
 * - NotificationManager.IMPORTANCE_DEFAULT -> 높은 중요도이며, 알림음이 울림.
 * - NotificationManager.IMPORTANCE_LOW     -> 중간 중요도이며, 알림음이 울리지 않음.
 * - NotificationManager.IMPORTANCE_MIN     -> 낮은 중요도이며, 알림음이 없고, 상태표시줄에 표시되지 않음.
 *
 *
 *
 * <Notification>
 * - Notification 은 알림에 출력될 이미지, 문자열 등의 정보를 담는 객체
 *
 * * 즉, Notification 에는 정보가 들어가야 한다. -> small Icon, title, when, text
 *
 *
 *              builder.setSmallIcon
 *
 *
 */