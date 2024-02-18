package com.tak.c80

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/**
 * 22. 유저 위치 정보를 앱에 적용해 보자.
 *
 * C80) Concepts of GeoLocation
 *
 * * 디바이스의 위치정보를 활용해서 다양한 서비스를 제공하는 방법에 대해 살펴 보자.
 *
 * * 아무래도 핸드폰이 handheld 디바이스 다 보니까, 이동성이 보장된 디바이스이고, 많은 앱들이 유저의 위치정보를 활용을 해서 다양한 서비스를 제공하고자 함.
 *
 * [GeoLocation Program]
 *
 *
 * <GeoLocation>
 *
 * - 유저의 위치를 이용한 서비스
 *
 * - android.permission.ACCESS_COARSE_LOCATION      : WiFi 또는 모바일 데이터 (또는 둘 다)를 사용하여 기기의 위치 획득. 정확도는 도시 블록 1개 정도의 오차 수준
 * - android.permission.ACCESS_FINE_LOCATION        : 위성, WiFi 및 모바일 데이터 등 이용 가능한 위치 제공자를 사용하여 최대한 정확하게 위치를 결정.
 * - android.permission.ACCESS_BACKGROUND_LOCATION  : Android 10(API 수준 29) 이상에서 백그라운드 상태에서 위치 정보 액세스시 필요.
 *
 *
 */