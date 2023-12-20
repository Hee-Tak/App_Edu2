package com.tak.c48

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var startView: ImageView
    lateinit var pauseView: ImageView
    lateinit var textView: TextView

    var isFirst = true                         //제어를 위한 Boolean 변수

    lateinit var asyncTask: MyAsyncTask


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startView = findViewById(R.id.main_startBtn)
        pauseView = findViewById(R.id.main_pauseBtn)
        textView = findViewById(R.id.main_textView)

        startView.setOnClickListener {
            if(isFirst){
                asyncTask.isRun = true
                asyncTask.execute()         //이 순간에 내부적으로 스레드가 만들어져서 doInBackground 함수가 실행된다고 보면 됨
                isFirst = false
            } else {
                asyncTask.isRun = true
            }
        }

        pauseView.setOnClickListener {
            asyncTask.isRun = false
        }

        asyncTask = MyAsyncTask()
    }


    inner class MyAsyncTask: AsyncTask<Void?, Int?, String>() {
        var loopFlag = true
        var isRun = false
        override fun doInBackground(vararg params: Void?): String {
            var count = 10
            while(loopFlag){
                SystemClock.sleep(1000)
                if(isRun){
                    count--
                    publishProgress(count)
                    if(count == 0){
                        loopFlag = false
                    }
                }
            }
            return "Finish!!"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)

            textView.setText(values[0].toString())
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            textView.setText(result)
        }

    }

}


/**
 * AsyncTask
 *
 * Thread-Handler 추상화
 * AsyncTask를 상속받은 클래스 작성
 *
 * doInBackground(Params... params) :   Thread에 의해 처리될 내용을 담기 위한 함수
 * onPreExecute()   :   AsyncTask의 작업을 시작하기 저에 호출. AsyncTask에서 가장 먼저 한번 호출
 * onPostExecute(Result result)  :  AsyncTask의 모든 작업이 완료된 후 가장 마지막에 한번 호출. doInBackground 함수의 최종 값을 받기 위해 사용.
 * onProgressUpdate(Progress... values) :   doInBackground 에 의해 처리되는 중간중간 값을 받아 처리하기 위해서 호출. doIngBackground 에서 publicProgress함수로 넘긴 값이 전달
 *
 *
 *  class MyAsyncTask: AsyncTask<Void?, Int?, String>()
 *  첫번째 타입 : Background 작업을 위한 doInBackground의 매개변수 타입과 동일. AsyncTask에 의해 Background 작업을 의뢰할 때 넘길 데이터의 타입. 없으면 Void 로 지정
 *  두번째 타입 : doInBackground 함수 수행에 의해 발생한 데이터를 publicProgress() 함수를 이용해 전달하는데 이때의 전달 데이터 타입. 데이터를 전달받을 onProgressUpdate 함수의 매개변수의 타입과 동일하게 지정.
 *  세번째 타입 : onPostExecute 의 매개변수 타입과 동일하게 지정. doInBackground 함수의 리턴 타입이며 리턴된 데이터가 onPostExecute 함수의 매개변수로 전달됨
 */