package com.tak.c49

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.ButtonBarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    val backgroundScope = CoroutineScope(
        Dispatchers.Default + Job()
    )

    lateinit var button: Button
    lateinit var resultView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        resultView = findViewById(R.id.resultView)

        button.setOnClickListener {
            backgroundScope.launch {
                var sum = 0L
                var time = measureTimeMillis {
                    for(i in 1..2_000_000_000){
                        sum += i
                    }
                }

                withContext(Dispatchers.Main) {
                    resultView.text = "sum : $sum"
                }

            }
        }
    }
}

/**
 * Coroutine - 가볍게 다뤄보자 (ANR 문제 해결차원에서 잠깐)
 *
 * Non-Blocking lightweight thread          정확히는 스레드 프로그램은 아니지만 일반 스레드 처럼, 어떤 특정 업무를 별개 수행 흐름으로 실행시키기 위한 용도
 *
 * - 경량이다.
 * - 메모리 누수가 작다
 * - 취소 등 다양한 기법을 지원한다              (스레드는 사실 취소같은게 불가능하다)
 * - 많은 JetPack 라이브러리에 적용되어 있다.
 * - implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'    -> dependency 관계 설정 -> build.gradle 파일에 등록하고 써줘야댐
 * implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
 *
 *
 * 기본적인 개념
 * 스코프 : 코루틴이 실행되는 영역
 * CoroutineScope 의 구현 클래스
 * GlobalScope, ActorScope, ProducerScope 등 제공
 *      val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
 *
 *
 *  디스패처 -> 이것들이 결국 UI를 출력 시킬 수 있는 메인 스레드를 지칭함 => 뷰와 관련되 작업을 할 때는 꼭 코루틴 내에서 이 Main Dispatcher에 의해서 처리가 되게 끔 해야한다.
 * Dispatchers.Main - 액티비티의 메인 스레드에서 동작하는 코루틴을 만들기 위한 디스패치
 * Dispatchers.IO - 파일에 읽거나 쓰기 또는 네트워크 작업 등에 최적화된 디스패치
 * Dispatchers.Default - CPU를 많이 사용하는 작업을 백그라운드에서 실행할 목적의 디스패치
 *
 * Coroutine 은 launch, async 등의 함수에 의해 실행
 * launch, async 는 Coroutine Builder
 *
 *      backgroundScope.launc {
 *
 *      }
 *
 *
 *
 *
 */