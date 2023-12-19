package com.tak.c44

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var count = 0
    lateinit var editView: EditText
    lateinit var countView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countView = findViewById(R.id.countView)
        editView = findViewById(R.id.edit)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            count++
            countView.text = "$count"
        }
        // EditText는 내부 기법상 좀 유지된다고 함. 화면을 회전한다 하더라도
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("count", count)
        outState.putString("edit", editView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        count = savedInstanceState.getInt("count")
        countView.setText("$count")
        editView.setText(savedInstanceState.getString("edit"))
    }
}

/**
 * 액티비티의 라이프 사이클 => 안드로이드 시스템에서 라이프사이클 관리 => 컴포넌트라 부름
 *
 * Activity Launched => onCreate(), onStart(), onResume() => Activity running
 * => onPause(), onStop(), onDestroy() => Activity Shutdown
 *
 * onStop() -> app process killed -> onCreate()
 * onStop() -> user navigates to the activity -> onRestart()
 *
 * LifeCycle
 * 활성상태 / 일시정지상태 (화면은 출력되나 유저가 이벤트 발생시킬 수 없음) / 비활성상태 (액티비티의 화면이 출력되고 있지 않음)
 *
 *
 */


/**
 * c45 Save Instance 액티비티 상태 저장 기법
 *
 * 액티비티가 종료되면 액티비티의 데이터는 모두 사라진다
 * 다시 실행시 복원 시켜야 하는 데이터가 있다면 상태 저장 기법을 이용해야 한다.
 * db에 저장하는 방법도 있지만 잠깐만 저장하면 되는 데이터를 db에 넣기엔 좀 그럴 때가 있다.
 *
 * **중요 -> 화면 회전할때 이런경우가 좀 발생한다고 함
 *      onCreate()
 *      onStart()
 *      onRestoreInstanceState()
 *      onResume()                  => 화면회전이 발생하면서 데이터 초기화가 된다. 이걸 해결해야한다는거 같음 (긍까 액티비티까 종료됐다가 회전되는 액티비티로 다시 켜진다 그걸 얘기하는거같음)
 *      onPause()
 *      onStop()
 *      onSaveInstanceState()
 *      onDestroy()                 => 종료       이 시점에 갖고있는 데이터가 다 사라짐
 *
 *
 *      화면회전이 되면서 다시 Start 되는거임
 *      onCreate()                  <= 생성
 *      onStart()
 *      onRestoreInstanceState()
 *      onResume()                  => 그래서 한바퀴 돌아서 이 상태로 돌아오는거같은 그렇게 되는거지
 *                                  => 그니까 다시 이 회전된 액티비티 화면이 나오기는 하지만 종료됐다가 다시 생성된거임. 그러면 이전꺼 액티비티의 데이터가 날라가고 없겠지.
 *
 *
 *
 *
 *  onCreate()                  얘 까지 Bundle이 주어지는 이유는 UI에 따라서 라이프사이클이 달라질 수 있어서
 *  onSaveInstanceState()
 *  onRestoreInstanceState()  얘네 써야댐 => 얘네의 매개변수 타입은 Bundle 임 => 대충 맵 객체 정도라고 생각하면 됨
 */


/**
 * Bundle
 * Bundle에 데이터 저장은 putString(), putInt() 등의 함수를 이용
 */