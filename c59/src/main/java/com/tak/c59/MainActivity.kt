package com.tak.c59

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val db: SQLiteDatabase = DBHelper(this).readableDatabase
            val cursor = db.rawQuery("select name from tb_member", null)
            if(cursor.moveToFirst()){
                Toast.makeText(this, "${cursor.getString(0)}", Toast.LENGTH_SHORT).show()
            }
            db.close()
        }
    }
}


/**
 * Part16 - 데이터베이스 프로그래밍으로 앱 데이터를 저장해보자
 * <Concepts of SQLite>
 */

/**
 * <SQLite>  -> sql 라이트 // 시퀄라이트     => 오픈소스 데이터베이스가 내장되어 있음.
 * - 데이터베이스로 데이터 영속화
 * - SQLite(www.sqlite.org) 은 오픈소스로 만들어진 데이터베이스로 관계형 데이터베이스
 * - 복잡하고 구조화된 어플리케이션 데이터 저장 및 관리
 *
 * 안드로이드 앱 입장에서는 db를 쓰는게 서버사이드 응용프로그램과 네트워킹인 셈.
 * 그리고 여기서 서버사이드 응용프로그램에서 서버사이드DB랑 DBMS가 이루어 지는 것
 *
 * 데이터를 저장한다 => (File read/write        Preference          Database)
 *                      이미지 데이터             설정 데이터          대량의 데이터를 구조화 시켜서 저장
 *
 * 여기서 Preference 가 데이터를 map 객체(key-value)로 저장하다 보니까, 설정 이외의 데이터를 간단하게 저장해서 이용하기에는 상당히 편함.
 * 근데 Preference가 갖고있는 한계가 있음. key-value 로 저장하다보니 대량의 데이터를 저장하기에는 부적절함.
 * 그리고 데이터를 구조화시키는 것도 부적절함.
 *
 *
 * 실전에서 개발하다보면 서버사이드DB 말고 로컬DB에 대량의 데이터를 저장하는 일이 꽤 되기도 함.
 * 서버사이드db에서 받아오는 데이터를 로컬db에 다시 저장하는 이유는 캐싱 차원에서 그러는 거임.
 * 그래서 데이터 캐싱 차원에서 들어가는게 데이터베이스 프로그램이다.
 *
 *
 * - SQLite Database 는 별도의 프로세스가 아닌 라이브러리를 이용
 * - 데이터베이스는 생성한 어플리케이션의 일부로 통합
 * - SQLite 를 이용한 데이터는 파일에 저장되며 /data/data/<package_name>/databases 폴더에 저장
 *
 *
 *
 * 일반적인 데이터베이스 (프로세스 DB)
 * -> 독립 프로세스로 동작하는 어플리케이션 데이터베이스
 * -> 어플리케이션에서 데이터베이스를 이용하려면 먼저 커넥션을 맺고 이용하는 것이 기본이다.
 *
 * 근데 안드로이드 에서 얘기하는 데이터베이스. SQLite (파일 DB)
 * 얘는 독립 프로세스로 동작하는 어플리케이션 데이터베이스가 아님. 그냥 라이브러리 ㅇㅇ
 * 어플리에키션에 그냥 통합되는 데이터베이스라고 보면 됨 => 그래서 커넥션이라는 개념이 없음.
 * 하지만 다른 db 이용하듯이 그대로 테이블만들고, row/column 구조 그대로 이용하고, sql문 그대로 쓰고 그럴거임.
 * 근데 실제 저장은 파일로 됨. 물론, 하나의 파일에 여러 테이블이 들어가 있을 것.
 * 그리고 이 데이터베이스 파일은 내장메모리 공간을 씀.
 * 즉, 외부 앱에서는 이용할 수 없다. 즉 <package_name>으로 되어있는 그 하위 폴더에서만 쓸수있다.
 */

//================================================================================================================================
/**
 * <SQLiteOpenHelper>
 *
 * - SQLiteDatabase 객체이용은 필수 => sql문을 실행시키겠다
 * - SQLiteOpenHelper 클래스는 앱을 위한 데이터베이스 관리적인 코드를 추상화 시킬 목적
 * - 데이터베이스에 테이블을 create 하거나 alter 혹은 drop 을 위한 코드 (이게 관리적인 코드들)
 *
 * * 꼭 써야하는 것은 아니지만, 쓰면 프로그램의 구조가 좋아진다.
 *
 * - SQLiteOpenHelper 클래스를 상속받아 작성
 *                                                                  (파일명)         (얘가 중요한데, 얘는 데이터베이스 버전 정보임. 이 정보가 바뀌는 순간에 감지가 내부적으로 이루어짐)
 *      class DBHelper(context: Context): SQLiteOpenHelper(context, "testdb", null, 1) {
 *
 *      }
 *
 * (자동호출되는 함수들)
 * - onCreate() : 앱이 인스톨된후 최초로 SQLiteOpenHelper 클래스가 이용되는 순간 한번 호출  => 이 때 거의 대부분 table create 같은 업무를 함.
 * - onUpgrade() : SQLiteOpenHelper 의 생성자에 지정한 DB 버전 정보가 변경될때마다 호출  => 얘는 scheme 변경
 *
 *
 * - SQLiteDatabase 객체를 SQLiteOpenHelper 클래스로 획득
 * - SQLiteOpenHelper 의 readableDatabase 혹은 writeableDatabase 프로퍼티로 SQLiteDatabase 객체를 획득
 *      val db: SQLiteDatabase = DBHelper(this).writeableDatabase
 *
 */