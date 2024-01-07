package com.tak.c53

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alertDialog = findViewById<Button>(R.id.alertDialogButton)
        val listDialogButton = findViewById<Button>(R.id.listDialogButton)
        val dateDialogButton = findViewById<Button>(R.id.dateDialogButton)
        val timeDialogButton = findViewById<Button>(R.id.timeDialogButton)

        alertDialog.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("test dialog")
                setIcon(android.R.drawable.ic_dialog_alert)
                setMessage("정말 종료하시겠습니까?")
                setPositiveButton("OK", null)
                setNegativeButton("Cancel", null)
                show()
            }
        }

        listDialogButton.setOnClickListener {
            val items = arrayOf<String>("사과", "복숭아", "수박", "귤", "라임", "레몬", "오렌지", "자몽")
            AlertDialog.Builder(this).run {
                setTitle("items test")
                setItems(items, object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Toast.makeText(this@MainActivity, "선택한 항목: ${items[p1]}", Toast.LENGTH_SHORT).show()
                    }
                })
                setPositiveButton("ok", null)
                show()
            }
        }

        dateDialogButton.setOnClickListener {
            DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    Toast.makeText(this@MainActivity, "$p1, ${p2+1}, $p3", Toast.LENGTH_SHORT).show()
                }
            }, 2024, 1, 7).show()
        }

        timeDialogButton.setOnClickListener {
            TimePickerDialog(this, object: TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    Toast.makeText(this@MainActivity, "$hourOfDay, $minute", Toast.LENGTH_SHORT).show()
                }
            }, 20, 23, true).show()
        }


    }
}

/**
 * Toast & Dialog
 */

/**
 * Toast
 *
 * - 화면 하단에 잠깐 보였다가 사라지는 문자열
 *      open static fun makeText(context: Context!, text: CharSequence!, duration: Int): Toast!
 *      open static fun makeText(context: Context!, resld: Int, duration: Int): Toast!
 *
 *      val LENGTH_LONG: Int        //5초후에 사라짐
 *      val LENGTH_SHORT: Int
 *
 *      Toast.makeText(this, "종료하려면 한번 더 누르세요.", Toast.LENGTH_SHORT).show()
 */


/**
 * Alert Dialog
 *
 * - 안드로이드 다이얼로그의 기본
 * - 단순히 문자열만 출력할 수도 있고 다양한 화면을 출력할 수도 있다.
 * - AlertDialog의 화면은 title, content, button 영역으로 구분
 *
 *
 * - AlertDialog.Builder 를 생성하고 Builder 의 setter 함수를 이용해 다이얼로그의 정보를 지정
 *      open fun setIcon(iconId: Int): AlertDialog.Builder!
 *      open fun setTitle(title: CharSequence!): AlertDialog.Builder!
 *      open fun setMessage(message: CharSequence!): AlertDialog.Builder!
 *
 * - 버튼은 최대 3개 까지만 추가  (ex-> more / no / yes)
 *      open fun setPositiveButton(text: CharSequence!, listener: DialogInterface.OnClickListener!):
 *      open fun setNegativeButton(text: CharSequence!, listener: DialogInterface.OnClickListener!):
 *      open fun setNeutralButton(text: CharSequence!, listener: DialogInterface.OnClickListener!):
 *                                      문자열                         핸들러
 *
 * - 목록을 제공 다이얼로그
 *      open fun setItems(items: Array<CharSequence!>!, listener: DialogInterface.OnClickListener!):
 *      open fun setMultiChoiceItems(items: Array<CharSequence!>!, checkedItems: BooleanArray!, listener: DialogInterface.OnMultiChoiceClickListener!):
 *      open fun setSingleChoiceItems(items: Array<CharSequence!>!, checkedItem: Int, listener: DialogInterface.OnClickListener!):
 *
 * - 날짜를 입력받기 위해 제공되는 다이얼로그
 *      DatePickerDialog(context: Context, listener: DatePickerDialog.OnDateSetListener?, year: Int, month: Int, dayOfMonth: Int)
 *
 * - 시간을 입력받기 위해 제공되는 다이얼로그
 *      TimePickerDialog(context: Context!, listener: TimePickerDialog.OnTimeSetListener!, hourOfDay: Int, minute: Int, is24HourView: Boolean)
 */

