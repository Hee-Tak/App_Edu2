package com.tak.c46

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.ImageView
import android.widget.TextView
import java.lang.String

class MainActivity : AppCompatActivity() {


    lateinit var startView: ImageView
    lateinit var pauseView: ImageView
    lateinit var textView: TextView

    var loopFlag = true
    var isFirst = true
    var isRun = false           // thread를 제어할 목적인 boolean 값들

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startView = findViewById(R.id.main_startBtn)
        pauseView = findViewById(R.id.main_pauseBtn)
        textView = findViewById(R.id.main_textView)

        startView.setOnClickListener {
            if(isFirst){
                isFirst = false
                isRun = true
                thread.start()
            } else {
                isRun = true
            }
        }

        pauseView.setOnClickListener {
            isRun = false
        }


    }

    var handler: Handler = object: Handler() {
        override fun handleMessage(msg: Message) {
            if(msg.what === 1){
                textView.setText(String.valueOf(msg.arg1))
            } else if(msg.what === 2){
                textView.text = msg.obj as String
            }
        }
    }


    var thread: Thread = object: Thread() {
        override fun run() {
            try {
                var count = 10
                while(loopFlag){
                    sleep(1000)
                    if(isRun){
                        count--
                        var message = Message()
                        message.what = 1
                        message.arg1 = count
                        handler.sendMessage(message) // 이 순간이 메인 스레드에 의뢰하는 순간
                        if(count == 0){
                            message = Message()
                            message.what = 2
                            message.obj = "Finish!!"
                            handler.sendMessage(message)
                            loopFlag = false
                        }
                    }

                }
            } catch(e: Exception){

            }
        }
    }


}


/**
 * ANR 문제를 해결하자
 * ANR : Activity Not Response => 그냥 응답없음이잖아 ㅡㅡ
 *
 * 유저 이벤트에 액티비티가 5초 이내에 반응하지 못하는 경우 발생하는 에러 (long time job)
 *
 * 나오는 용어
 * Event Queue 이거는 이벤트큐에 이벤트가 담기고 5초내에 돼야하는데 안되면 anr 뜬다는거지
 * mainThread 그니까 이얘기가 왜 나오냐하면 long time job을 mainThread에서 실행중인데 유저가 중간에 User Event를 걸 수 있다는거지
 * 즉, 이때 에러가 날 수 있다.
 *
 * 즉 이런식으로 시간이 오래걸리는 업무가 걸려있다고하면 ANR 이슈를 고려해서 개발을 해야한다 ( 뭐 로딩액티비티를 일부러 추가한다던가 해야겠지?)
 *
 * 대표적인 이런 시간이 오래걸리는 업무 LONG TIME JOB => 네트워크 업무
 * 대부분의 어플리케이션이 서버랑 연동을 한다. 그래서 프론트엔드 백엔드 구조로 개발을 한다.
 * 그러다보니, 우리가 만드는 프론트엔드. 안드로이드 앱 입장에서 주된 목적이 뭐냐? => 화면을 제공해주고, 비즈니스 데이터를 화면에 뿌리는 역할
 * 이러한 데이터는 어디서 발생하냐, 어느 업무로직에서 나오냐 => 백엔드 (서버측의 어플리케이션에서 진행이 된다 뭐 그런거지)
 *
 * 네트워크 프로그래밍이 들어가면 무조건 ANR 문제가 발생할 수 있다고 가정하고 개발을 진행해야 한다.
 *
 * 일반적으로 액티비티에서 서버랑 네트워킹 하는 프로토콜 대부분이 HTTP 프로토콜을 이용해서 네트워킹을 할 것. (아주 간헐적으로 소켓 연결 프로그램이 진행)
 * HTTP 통신 => 커넥션을 맺어서 서버랑 request responce 한 다음에 커넥션이 끊어지는 구조
 * 소켓 연결 프로그램 => 커넥션 따로하고 / 커넥션을 유지한 상태에서 필요한 순간 데이터를 write하고 read 하는 식의 프로그램
 *
 * 모바일 네트워킹이라 수시로 네트워킹이 안될 것이기 때문에 무조건 anr 이슈를 고려해서 개발해야 한다. (모바일이라 네트웍 상황이 불안정하다는 가정하에 개발)
 */


/**
 * 해결 방법
 *
 * Thread - Handler 로 해결        (스레드-핸들러 구조)
 * AsyncTask 로 해결               (스레드 핸들러의 추상화 개념 정도) 안드로이드 11버전으로 올라오면서 이 부분이 deprecation 됐음
 * Coroutine 으로 해결              현재는 코루틴으로 개발할 것을 권장
 */


/**
 * C47 - Multi Thread Programming 으로 해결
 *
 *  Main Thread (UI Thread) -> 화면 처리 및 이벤트 처리
 *  Thread -> 업무 처리 (시간이 걸리는..등등 다른 업무 처리)
 *
 *  개발자 스레드 내에서는 화면 출력 요소인 뷰 접근이 안되게끔 프로그램을 짜야한다.
 *  즉 메인스레드에서만 뷰가 관리되게끔 프로그램을 작성해야 한다.
 *  근데 다 액티비티로 만드는거라, 뭔가 데이터를 다룬다 이러면 다 화면에 표시해야하고 하는거라 뷰를 안쓸수가 없음
 *  그래서 이걸 어떡하라는거냐?
 *  개발자 스레드에서 업무 처리를 하다가 화면을 구성하는 뷰를 이용할 일이 있다고 한다면!
 *  => 직접 접근하지 말고 ** Handler ** 클래스를 이용해서 메인스레드에 의뢰해서, 메인 스레드에 의해서만 뷰가 관리되게끔 개발하는 구조
 *
 *  => 이게 Thread-Handler 구조
 *
 *  -> 이건 프론트엔드 쪽, 어플리케이션에 일반적인 스레드 패턴임. 안드로이드만 이러는건 아님
 *  화면을 책임지는 스레드와 비즈니스 업무 로직을 처리하는 스레드로 분리시켜서하는 유명한 스레드 패턴임
 */

/**
 * Handler
 *
 * Handler 로 main thread 에게 작업 의뢰
 *
 * sendMessage(Message msg) :
 * sendMessageAtFrontOfQueue(Message msg) : View 작업에 대한 의뢰가 반복적으로 발생한 경우 UI Thread에서 순차적으로 처리하는데 이번 의뢰를 가장 먼저 처리하라는 요청
 * sendMessageAtTime(Message msg, long uptimeMillis) : 의뢰를 지정된 시간에 수행
 * sendMessageDelayed(Message msg, long delayMillis) : 지연시간후에 수행 시켜 달라는 요청
 * sendEmptyMessage(int what) : 데이터 전달 없이 의뢰하는 경우
 *
 *
 * Message 객체는 main thread에게 넘기는 데이터.
 *
 * what : int 형 변수로 구분자. 개발자가 임의 숫자값으로 요청을 구분하기 위해서 사용
 * obj : UI thread 에게 넘길 데이터. Object 타입의 변수
 * arg1, arg2 : UI thread 에게 넘길 데이터, int 타입으로 간단한 숫자값은 arg1, arg2 변수에 담아 전달.
 *
 *
 *
 * main Thread 에서 Handler 객체의 HandleMessage 호출
 */