package com.tak.c60

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * C60
 * <rawQuery, execSql>
 *
 * <SQLiteDatabase>
 * - SQLite를 이용하기 위한 최소한의 API
 * - openOrCreateDatabase() 함수를 이용해 획득
 * - SQLiteOpenHelper 객체를 이용해 획득
 *      val db = openOrCreateDatabase("testdb", Context.MODE_PRIVATE,null)
 *
 * - public void execSQL (String sql, Object[] bindArgs)
 *      db.execSQL("create table USER TB (" +
 *          "  id integer primary key autoincrement," +
 *          "name not null," +
 *          "phone)")
 *
 *
 *      db.execSQL("insert into USER_TB (name, phone) values (?,?)", arrayOf<String>("TAK", "0101111"))
 *
 *
 * - public Cursor rawQuery (String sql, String[] selectionArgs)  => 리턴값(Cursor) 때문에 select 쓰는데에 특화됨
 *      val cursor = db.rawQuery("select * from USER_TB", null)
 *
 *
 * - rawQuery() 함수의 리턴 값은 Cursor 객체이며 select 된 row 의 집합객체
 * - Cursor 객체를 움직여 row 를 선택하고 선택된 row 의 column data 를 획득
 *      public abstract boolean moveToFirst()
 *      public abstract boolean moveToLast()
 *      public abstract boolean moveToNext()
 *      public abstract boolean moveToPosition(int position)
 *      public abstract boolean moveToPrevious()
 *      //선택되면 true가 리턴, 선택되지 못하면 false 가 리턴됨
 *
 *
 *      while(cursor.moveToNext()) {
 *          val name = cursor.getString(0)
 *          val phone = cursor.getString(1)
 *      } //-> 이런식으로 (선택된)전체 row를 핸들링
 */