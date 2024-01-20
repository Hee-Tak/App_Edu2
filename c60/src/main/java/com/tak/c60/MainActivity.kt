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
 */