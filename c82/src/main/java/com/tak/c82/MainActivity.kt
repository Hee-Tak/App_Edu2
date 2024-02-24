package com.tak.c82

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    lateinit var resultView: TextView
    lateinit var providerClient: FusedLocationProviderClient
    //지금 이건, 표준 플랫폼에서 제공되는 API가 아니라,
    //Fused Location Api 라고 해서, 구글에서 제공되는 api를 이용하는것이기 때문에,
    //라이브러리 dependency 관계를 설정해주어야 함. -> build.gradle 파일 에서 설정
    // 그러고나면, 라이브러리 등록 가능
    // providerClient 얘 자체는 위치값을 획득할 때 쓰는 api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultView = findViewById(R.id.resultView)


        val apiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(connectionCallback) //준비한 callback 등록
            .addOnConnectionFailedListener(connectionFailedCallback)    //실패시의 콜백
            .build()
            //이렇게 해두고. 이제 이 객체한테 위치 추적좀 해! 하고 명령을 내리면 알아서 콜백이 호출될 것. (connectionCallback or connectionFailedCallback 둘중 하나가 호출이 될 것)


        providerClient = LocationServices.getFusedLocationProviderClient(this)

        val launcher = registerForActivityResult(       //퍼미션 관련 준비
            ActivityResultContracts.RequestPermission()
        ) {
            if(it) {
                apiClient.connect()
            } else {
                Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show()
            }
        }
        //런처 준비 완료


        //퍼미션 상태 파악
        val status = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")
        if(status == PackageManager.PERMISSION_GRANTED){
            apiClient.connect()
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }





    val connectionCallback = object : GoogleApiClient.ConnectionCallbacks {
        override fun onConnected(p0: Bundle?) {
            //어떤 프로바이더가 이용 가능한 상태가 됐을 때, 호출되는 함수
            providerClient.lastLocation.addOnSuccessListener {      //지금 얘가 빨간건 위치값 퍼미션 이슈인건데, 퍼미션을 checkSelfPermission 으로 이 부분을 감싸면 에러는 사라지긴 하겠지만, 여기서는 어차피 다른데에서 처리할 것.
                val latitude = it?.latitude
                val longitude = it?.longitude
                resultView.text = " latitude : $latitude \n longitude : $longitude"
            }
        }

        override fun onConnectionSuspended(p0: Int) {
            //얘가 콜 된다는 것은, 이용하고 있던 프로바이더가 갑자기 이용 불가능한 상태가 됐을 때 그걸 알려주기 위해 호출이 된다.
        }
    }

    val connectionFailedCallback = object : GoogleApiClient.OnConnectionFailedListener {
        //상황에 따라서 폰의 모든 프로바이더가 이용 불가능한 상황이 있을 수도 있다.
        override fun onConnectionFailed(p0: ConnectionResult) {
            //Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()

        }
    }


    // Duplicate class found 에러 때문에 build.gradle 파일 수정했음.
}


/**
 * c82) Fused API                   //위치 정보를 획득할 수 있는 구글의 api
 *
 *
 * * LocationManager 를 쓸 땐, 이 Location Provider 를 어느 프로바이더를 이용할 거냐? 라는 지정이 의외로 복잡해질 수 가 있다.
 *
 * <Fused API>
 *
 * - 위치정보를 획득할 때 여러가지 상황을 고려
 *
 * - 낮은 전력 소모
 * - 정확도 향상
 * - 간단한 APIs           //전력과 정확도를 고려하여 간단하게 작성하게끔 하기 위한 것이 Fused Location API.
 *
 * - 정보 획득과 관련된 코드의 복잡함을 줄이기 위해 구글에서 제공되는 API 가 Fused Location Provider         -> 코드의 복잡함을 줄이기 위한 구글에서 제공되는 별도의 API 구나.
 *              implementation 'com.google.android.gms:play-services:12.0.1'
 *              //라이브러리 dependency 관계 설정하고 사용.
 *
 *
 *
 * - Fused Location Provider 의 핵심 클래스 (2가지) -> (FusedLocationProviderClient / GoogleApiClient)
 *
 * - FusedLocationProviderClient    : 위치 정보 획득                          (이걸 위해선 프로바이더가 결정이 나야하고, 그 프로바이더가 이용 가능 한지, 등을 계속 체크해 줘야함. -> 이 역할을 ApiClient 가 맡음)
 * - GoogleApiClient                : 위치 정보 제공자 이용 준비, 다양한 콜백 제공
 *
 * - GoogleApiClient 에는 GoogleApiClient.ConnectionCallbacks 와 GoogleApiClient.OnConnectionFailedListener 인터페이스를 구현한 객체를 지정
 *
 *              val connectionCallback = object : GoogleApiClient.ConnectionCallbacks {
 *                  override fun onConnected(p0: Bundle?) {
 *                      //위치 정보 제공자가 사용 가능 상황이 된 순간
 *                          //위치 정보 획득 .....
 *                  }
 *
 *                  override fun onConnectionSuspended(p0: Int) {
 *                      //위치 정보 제공자가 어느순간 사용 불가능 상황이 될 때...
 *                  }
 *              }
 *
 *
 *              val onConnectionFailedCallback = object : GoogleApiClient.OnConnectionFailedListener {
 *                  override fun onConnectionFailed(p0: ConnectionResult) {
 *                      //가용할 위치 제공자가 없는 경우
 *                  }
 *              }
 *
 *              //api 클라이언트 객체 초기화
 *              val apiClient: GoogleApiClient = GoogleApiClient.Builder(this)          //GoogleApiClient : 구글의 다양한 서비스를 활용할 수 있는 클라이언트 (위치정보제공 뿐만 아니라.)
 *                  .addApi(LocationServices.API)                                       //어떤 서비스를 쓸거냐 -> 위치 정보 서비스(api)를 쓰겠다. LocationServices.API
 *                  .addConnectionCallbacks(connectionCallback)                         //인터페이스를 구현한 객체를 함수가지고 등록을 시켜주면 (우리가 만든 객체 : connectionCallback, onConnectionFailedCallback) 함수가 오버라이드 받아서 작성이 되어 있다....
 *                  .addOnConnectionFailedListener(onConnectionFailedCallback)          //위치정보 제공자를 판단하면서 알아서 이 함수들을 콜 해줘서 위치정보 제공자를 하나 이용가능한지 여부.. 이런 상황을 우리의 콜백함수로 호출해주면서 알려주게 되어 있다.
 *                  .build()
 *
 *
 *
 * - GoogleApiClient 객체에 위치 제공자를 판단
 *
 *              apiClient.connect()                 //위치 정보 제공자를 판단. (네트워크 커넥트는 아니지만, 개념상 이렇게.)
 *              // 이렇게 해주면 아까 콜백함수를 통해 콜에서 알려주게 됨.
 *
 *
 * * 이용 불가능한 상태라면 토스트를 띄우던가 할것이고. 이용 가능한 상태라면 아래로.
 *
 * - FusedLocationProviderClient 의 getLastLocation() 함수를 이용해 위치 획득      (위치값은 또 콜백으로 전달이 된다.)
 * - 결과 값은 addOnSuccessListener() 함수에 등록한 OnSuccessListener 구현 객체의 onSuccess() 함수가 호출되며 전달
 *
 *              providerClient.getLastLocation().addOnSuccessListener(
 *                  this@FusedActivity,
 *                  object : OnSuccessListener<Location> {
 *                      override fun onSuccess(location: Location?) {
 *                          val latitude = location?.latitude
 *                          val longitude = location?.longitude
 *
 *                      }
 *                  })
 *
 *
 */