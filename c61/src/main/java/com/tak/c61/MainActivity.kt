package com.tak.c61

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * C61
 * <insert, update, delete, query>
 *
 * - insert(), update(), delete(), query() 함수를 이용한 SQL문 실행
 * - SQL 문에 들어갈 부분을 매개변수로 대입하면, SQL 문을 만들어 실행시켜 주는 함수 => 개발자가 직접 SQL문을 완성하는게 아니라, SQL문을 만들기 위한 정보 정도를 준다고 보면 된다.
 *          public long insert (String table, String nullColumnHack, ContentValues values)
 *                                            ->획득하고자 하는 컬럼명(조건)    -> 값
 *          public int update (String table, ContentValues values, String whereClause, String[] whereArgs)
 *          public int delete (String table, String whereClause, String[] whereArgs)
 *          public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
 *
 *
 * - ContentValues 는 insert, update 를 위한 컬럼 데이터 집합 객체
 * - Map 객체처럼 키-값 형태로 데이터 여러 건을 ContentValues 에 등록  => 키 = 컬럼명
 *          val values = ContentValues()
 *          values.put("name", "tak")
 *          values.put("phone", "0101112")
 *          db.insert("USER_TB", null, values)
 *
 *
 *
 * <query()>
 * - table : select 하고자 하는 테이블 명
 * - columns : 획득하고자 하는 column 명, 배열데이터로 column 명 지정
 * - selection : select 문의 where 뒤에 들어갈 문자열 (=> 없으면 null 주면 됨)
 * - selectionArgs : selection 에 지정된 문자열이 데이터가 들어갈 자리를 ? 로 표현했다면 ? 에 들어갈 데이터
 * - groupBy : select 문의 group by 뒤에 들어갈 문자열
 * - having : select 문의 having 조건
 * - orderBy : select 문의 order by 조건
 */

