package com.tak.c80

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    lateinit var resultView: TextView
    lateinit var manager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultView = findViewById(R.id.resultView)
        manager = getSystemService(LOCATION_SERVICE) as LocationManager

        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if(isGranted){
                getLocation()
            } else {
                Toast.makeText(this, "denied..",  Toast.LENGTH_SHORT).show()
            }
        }

        val status = ContextCompat.checkSelfPermission(this,
            "android.permission.ACCESS_FINE_LOCATION")
        if(status == PackageManager.PERMISSION_GRANTED){
            getLocation()
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) //android 쪽 Manifest
        }

        val next = findViewById<Button>(R.id.nextButton)
        next.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }




    }

    fun getLocation() {
        val location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) //지금 현재로썬 이 함수 내에서는 권한부여(permission 쪽)이 안돼있어서 빨간줄 뜨는것. 어차피 위에 코드쪽 가면 뜸

        //location?.let {//이 블록에 들어왔다는 것은, null이 아니라는 것.
        //    val latitude = location.latitude //위도
        //    val longitude = location.longitude //경도
        //    val accuracy = location.accuracy
        //    val time = location.time
        //
        //    resultView.text = " 위도: $latitude \n 경도: $longitude \n 오차범위: $accuracy \n 시각: $time \t location: $location"
        //}

        if(location != null) {
            updateLocationInfo(location)
        } else {
            resultView.text = "위치 정보를 가져올 수 없습니다."
        }

    }

    private fun updateLocationInfo(location: Location) {
        val latitude = location.latitude //위도
        val longitude = location.longitude //경도
        val accuracy = location.accuracy
        val time = location.time

        resultView.text = " 위도: $latitude \n 경도: $longitude \n 오차범위: $accuracy \n 시각: $time \t location: $location"
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
 * * 퍼미션 선언
 * - android.permission.ACCESS_COARSE_LOCATION      : WiFi 또는 모바일 데이터 (또는 둘 다)를 사용하여 기기의 위치 획득. 정확도는 도시 블록 1개 정도의 오차 수준
 * - android.permission.ACCESS_FINE_LOCATION        : 위성, WiFi 및 모바일 데이터 등 이용 가능한 위치 제공자를 사용하여 최대한 정확하게 위치를 결정.
 * - android.permission.ACCESS_BACKGROUND_LOCATION  : Android 10(API 수준 29) 이상에서 백그라운드 상태에서 위치 정보 액세스시 필요.
 *
 *
 * * 유저의 위치정보 = 위경도 ==활용==> 지도앱, 길안내, 게임,... 등등
 * * 그냥 서버 연동으로 해서 서버사이드에 데이터를 뿌려주는 그런 앱이라 하더라도 위치정보를 활용할 수 있을까? -> 주소문자열을 클릭하면 지도로 보여지게 뭐 그렇게 할 수 있다.
 * * 그리고 인스타도 보면 사용자 위치정보 뜨잖아, 그게 아니더라도 해당 관련 데이터가 생산된 위치 정보를 내부적으로 이용할 수 있음
 *
 *
 * - 위치 제공자 (Location Provider)
 *
 * - GPS        : GPS 위성을 이용하여 위치 정보 획득 -> 정확도 높음. 오차범위 10~15m 정도. -> 음영지역이 너무 많아서 안좋긴하다.
 * - Network    : 이동통신사 망 정보를 이용하여 위치 정보 획득 -> 기지국 정보. 오차범위 1km 정도
 * - Wifi       : 와이파이의 AP 정보를 이용하여 위치 정보 획득
 * - Passive    : 다른 앱에서 이용한 마지막 위치 정보 획득
 *
 * * 이런걸 지정하는게 좀 까다로울 수 있는데 (각각이 특징이 다 다르고 장단점이 다 다르기 때문) 이걸 지원하는게 Fused Location Api
 *
 * * 가장 기초적으로 위치를 추적하겠다라고 하면 시스템 서비스를 이용하면 된다. (LocationManger 라고 하는 시스템 서비스가 제공됨)
 *
 *
 * - 폰에 어떤 위치정보 제공자가 있는지 파악
 *
 *          val manager = getSystemService(LOCATION_SERVICE) as LocationManager
 *
 *          val providers = manager.allProviders
 *          for (provider in providers) {
 *              result += "$provider,"
 *          }
 *
 *
 *
 *
 * - 기본적으로 위/경도 는 도/분/초 로 표시되는데 일반적인 시스템에서는 실수로 사용 => 이걸 그대로 적용하기엔 너무 어려워서 "실수"로 바꿔서 사용 (어차피 자동으로 됨)
 * - 위도 : 90도 ~ 0도 ~ -90도 (0도 : 적도)
 * - 경도 : 180도 ~ 0도 ~ -180도 (0도 : 그리니치 천문대)
 * - 37도 30분 30초 -> 37.5 => 보통 소수점 6자리까지 끊어서 이용함 (그 밑은 위치 데이터로서 가치가 없다)
 *
 *
 *
 * - 위치정보를 추적하기 위한 두 가지 방법
 * - LocationManager    -> 시스템 서비스를 이용하는 방법 , 플랫폼에서 제공되고 있는 API (즉, 표준 API)
 * - Fused API          -> 구글에서 제공. 이걸 이용하면 몇몇 프로그램을 좀 더 간단하게 만들 수 있다.
 *
 *
 */

/**
 * c81) LocationManager
 *
 * <LocationManager>
 *
 * - 플랫폼 API 에서 제공되는 시스템 서비스 (표준 API)  => 그렇기에, 특별하게 라이브러리 dependency 관계 설정할 필요도 없고, API 레벨 1 버전부터 제공이 돼서 지금까지 변경된 적이 없다.
 *
 *          val manager = getSystemService(LOCATION_SERVICE) as LocationManager
 *
 * - 위치 정보 획득은 LocationManager 의 getLastKnownLocation() 함수를 이용
 *
 *          val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) //LocationManager.GPS_PROVIDER : GPS 프로바이더를 이용하여 위치값을 획득. 로케이션 프로바이더
 *          // 그러나 GPS 프로바이더를 이용할 수 없는 상황에서는 위치 값 획득이 안된다는 것. (EX-지하철내부, 건물내부) -> 그래서 추후에는 이 매개변수 부분을 복잡하게 지정해줘야할 수도 있다.
 *          // Location 객체타입으로 전달이 되는데, 일종의 VO 객체. (정보가 들어가 있는 객체)
 *
 * - 결과값은 Location 객체로 전달       그 다음, 온갖 get 함수로 뽑아내면 된다.
 * - getAccuracy()  : 정확도       -> 오차범위를 뜻함.
 * - getLatitude()  : 위도        -> 실수 값
 * - getLongitude() : 경도        -> 실수 값
 * - getTime()      : 획득 시간
 *
 *
 * - 지속적으로 위치를 획득해야 한다면 LocationListener 를 이용
 *
 *          val listener: LocationListener = object : LocationListener { //object means 객체
 *              override fun onLocationChanged(location: Location) {
 *                  TODO("Not yet implemented")
 *              }
 *
 *              override fun onProviderDisabled(provider: String) {
 *                  super.onProviderDisabled(provider)
 *              }
 *
 *              override fun onProviderEnabled(provider: String) {
 *                  super.onProviderEnabled(provider)
 *              }
 *          }
 *                                                                        시간    오차범위 (미터단위)
 *          manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10_000L, 10f, listener)        //requestLocationUpdates 를 이용해서 매개변수 listener 에 전달. 그럼 매개변수 listener에게 반복적으로 함수 호출하면서 위치 값을 제공 해 줌.
 *          //위의 Location Provider (GPS_PROVIDER) 가 갑자기 이용 불가능한 상황이 될 수도 있다. -> 이때 onProviderDisabled 호출. -> 다시 이용가능해지면 onProviderEnabled
 *
 *          //.....
 *
 *
 *          manager.removeUpdates(listener)         // 더이상 위치값을 안받겠다.
 *
 *
 *
 *
 */