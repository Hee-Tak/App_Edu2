package com.tak.c52

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vibButton = findViewById<Button>(R.id.vibration)
        val beepButton = findViewById<Button>(R.id.beep)

        vibButton.setOnClickListener {
            val vibrator = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                val vibManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibManager.defaultVibrator
            } else {
                getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))

            } else {
                vibrator.vibrate(500)
            }

            beepButton.setOnClickListener {
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(applicationContext, uri)
                ringtone.play()
            }

        }
    }
}

/**
 * Vibration & Beep
 *
 * Vibration
 * - 퍼미션 필요 : <uses-permisson android:name="android.permission.VIBRATE"/>
 *
 * - Vibrator 라는 시스템 서비스 이용
 * - API31 이전까지는 Vibrator 라는 시스템 이용 // APL31 부터는 VibratorManager 라는 시스템 서비스 이용
 *
 *  val vibrator = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
 *      val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
 *      vibratorManager.defaultVibrator;
 *  } else {
 *      getSystemService(VIBRATOR_SERVICE) as Vibrator
 *  }
 *
 *
 *
 *  - API Level 26 이전 버전에서 사용되던 함수
 *      open fun vibrate(milliseconds: Long): Unit
 *      open fun vibrate(pattern: LongArray!, repeat: Int): Unit
 *
 *  - API Level 26 부터 사용되는 함수
 *      open fun vibrate(vibe: VibrationEffect!): Unit
 *  - VibrationEffect 객체 획득 방법
 *      open static fun createOneShot(milliseconds: Long, amplitude: Int): VibrationEffect!
 *      open static fun createWaveform(timings: LongArray!, amplitudes: IntArray!, repeat: Int): VibrationEffect!
 *
 *
 *  - 두 가지 코드를 같이 작성하고 싶다면 아래와 같이 빌드 버전에 따른 분기를 나눠서 작성
 *  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 *      vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
 *  } else {
 *      vibrator.vibrate(500)
 *  }
 *
 *
 */


/**
 * Beep
 * - 시스템에 등록된 알림음 플레이
 * - 시스템에는 알림(NOTIFICATION), 알람(ALARM), 링톤(RINGTONE)등의 음이 준비되어 있다.
 *
 *      val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
 *      val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
 *      ringtone.play()
 *
 *
 *  - 임의의 음원을 비프음으로 사용한다면 raw 폴더 이용 (res\raw)
 *
 *  play
 *      val player: MediaPlayer = MediaPlayer: create(this, R.raw.fallbackring) // -> 일반적으로 음원 혹은 영상을 플레이 하기 위한 api라고 보면 됨
 *      player.start()
 *
 */