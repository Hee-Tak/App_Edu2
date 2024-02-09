package com.tak.c75

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "one"
                val channelName = "channel name"
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.description = "one description"
                manager.createNotificationChannel(channel) //manager 에 채널 등록

                builder = NotificationCompat.Builder(this, channelId)
            } else {
                builder = NotificationCompat.Builder(this)
            }
            //여기까지 빌더 만듦


            builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
            builder.setWhen(System.currentTimeMillis())
            builder.setContentText("Text")
            builder.setContentTitle("Title")

            val actionIntent = Intent(this, DetailActivity::class.java)
            val actionPendingIntent = PendingIntent.getActivity(this, 20, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(actionPendingIntent)

            val actionIntent2 = Intent(this, DetailActivity::class.java)
            val actionPendingIntent2 = PendingIntent.getActivity(this, 20, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.addAction(
                NotificationCompat.Action.Builder(
                    android.R.drawable.stat_notify_more,
                    "Action",
                    actionPendingIntent2
                ).build()
            )

            val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.logo_1)
            val style = NotificationCompat.BigPictureStyle()
            style.bigPicture(bigPicture)
            builder.setStyle(style)

            manager.notify(1, builder.build())

        }

    }
}

/**
 * c75) Notification 구성
 *
 * * Notification 을 구성하는 다양한 방법에 대해서 알아 보자.
 * * Notification 은 코어 정보 뿐만 아니라, 다양한 정보를 설정할 수 있다.
 *
 */

/**
 * <터치 이벤트>
 *
 *          static fun getActivity(context: Context!, requestCode: Int, intent: Intent!, flags: Int): PendingIntent!
 *          static fun getBroadcast(context: Context!, requestCode: Int, intent: Intent!, flags: Int): PendingIntent!
 *          static fun getService(context: Context!, requestCode: Int, intent: Intent!, flags: Int): PendingIntent!
 *
 *
 * // 우리 Activity, BroadcastReceiver, Servcie 얘네를 구동하도록 이벤트를 잡을 수 있는데, 그럴려면 당연히 인텐트가 발생이 돼야 된다.
 * // 인텐트를 띄우는거는 시스템에서 띄우는 거다. => 왜냐하면 Notification 이 시스템 창이다 보니까 이벤트가 시스템에서 발생하기 때문
 * // 그래서 우리는 이 인텐트를, Notifictaion이 터치됐을 때 띄워줘. 하고 의뢰 하는 정도가 됨.
 *
 * // PendingIntent : 인텐트 정보를 가지고 있는데, 아직 발생하지 않은 인텐트를 뜻함.
 * // 그래서 이 PendingIntent 로 시스템에 등록하면, 시스템에서 실제 이벤트가 발생됐을 때, 여기에 있는 인텐트대로 발생을 시켜준다는 개념.
 *
 * > 만약에 Notification 이 떴다고 하면, small Icon 만 상단 상태바에 보이는 상태.
 * > 이 상태바를 끌어 내려서 확장 컨텐츠를 띄울 수 있다.
 * > 이 확장 컨텐츠를 터치했을 때, 이벤트 처리를 해야한다. => 대부분 우리 앱의 컴포넌트를 실행시키는 식으로 한다.
 * * 이 Notification 이 뜨는 건, 우리 앱의 컴포넌트가 아님. 시스템 창이다.
 *
 *
 *          val intent = Intent(this, DetailActivity::class.java)
 *          val pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT)
 *          builder.setContentIntent(pendingIntent) //Notification 을 만드는 builder
 *                                                  //setContentIntent 즉, 컨텐츠를 터치했을 때, 이벤트를 등록하겠다고 해서 PendingIntent 객체를 대입을 시켜주면 됨.
 *
 */

/**
 * <액션>
 *
 * - 알림에 최대 3개까지의 유저 이벤트를 위한 액션을 추가
 *
 *              (icon) Test10  오전 10:33                 ^
 *              Content Title
 *               Content Massage
 *                                      Action
 *
 * > (Notification 하단에 들어가는 일종의 버튼) (유저가 이걸 터치를 했을 때, 이벤트 처리를 하겠다 라는 것)
 * > 그럼 우리 쪽에서는 액티비티를 만들거나, 리시버를 만들거나, 서비스를 만들거나 해야 할 것
 * > 액션을 눌렀을때 이 만들어 놓은 액티비티, 리시버, 서비스가 실행이 되게 해야함.
 * > 그러면 또 인텐트가 발생이 돼야 되는데. 이건 우리가 발생시키는게 아니라, 액션에 등록해서 시스템에 의해서 발생되게 끔 만들어줘야 된다.
 * > 그래서 다시 PendingIntent 로 등록을 해야 한다.
 *
 *
 *          val actionIntent = Intent(this, DetailActivity::class.java)                                                         //인텐트를 하나 준비
 *          val actionPendingIntent = PendingIntent.getActivity(this, 20, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)      // 그다음, PendingIntent를 준비
 *          builder.addAction(
 *              NotificationCompat.Action.Builder(
 *                  android.R.drawable.stat_notify_more,        //<- 아이콘
 *                  "Action",                                   //<- 이 부분은 액션 문자열.
 *                  "actionPendingIntent
 *              ).build()
 *          )
 *
 * > 이런식으로 하면, 액션이 하나 추가가 된다.
 */

/**
 *  <Progress>
 *
 * - 알림을 이용해 일의 진행 상황
 * * 프로그레스바 를 칠해주는 경우 (ex-파일 업로드 진행 상황)
 *
 *          builder.setProgress(100, 0, false)          //max : 100 , 초깃값 : 0 => 즉 이 상태는 아무것도 칠해지지 않은 프로그레스바
 *          manager.notify(11, builder.build())
 *
 *          thread {
 *              for(i in 1..100){
 *                  builder.setProgress(100, i, false)
 *                  manager.notify(11, builder.build())
 *                  SystemClock.sleep(100)
 *              }
 *          }
 */


/**
 * <Style>
 *
 * - BigPictureStyle, BigTextStyle, InboxStyle, MessageStyle
 *
 * * 타이틀 문자열과 텍스트 문자열 이외에, 다양한 스타일 추가 가능. 버전이 업 되면서 이 스타일들이 계속 추가가 됨.
 *
 *          val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.logo_1)
 *          val bigStyle = NotificationCompat.BigPictureStyle()         //빅픽쳐스타일 생성
 *          bigStyle.bigPicture(bigPicture)                         //정보 전달
 *          builder.setStyle(bigStyle)                              // set
 */