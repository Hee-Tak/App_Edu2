package com.tak.c50

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //test2..........
        val requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if(it){
                Toast.makeText(this, "granted...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "denied...", Toast.LENGTH_SHORT).show()
            }
        }



        val status = ContextCompat.checkSelfPermission(
            this,
            "android.permission.ACCESS_FINE_LOCATION"
        )
        if(status == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "granted...", Toast.LENGTH_SHORT).show()
        } else {
            //test1...
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf<String>("android.permission.ACCESS_FINE_LOCATION"),
//                100
//            )

            //test2....
            requestPermissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) //Manifest 이걸 Android Manifest 로 잡아야됨. 작성 과정에서.
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "granted...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "denied...", Toast.LENGTH_SHORT).show()
        }
    }
}

/**
 * Permission -> 앞으로 계속 다룰 거임. 거의 안드로이드 앱 개발의 기본
 *
 * A app                <-          B app
 * Activity
 * ContentProvider
 *
 * A 입장에서 자신들이 갖고있는 몇몇 컴포넌트들을 외부 어플리케이션에서 실행시키고 싶은데 그러면 인텐트 필터를 달아줘야함
 * 그래서 암시적인 인텐트에 의해서 실행될 수 있도록 준비를 해줘야 함
 *
 * 근데 여기서, 보안을 걸고싶다? => 여기서 특정 보안을 통과하는 이런 어플리케이션과만 연동을 하고 싶다?
 * 이럴 때, 퍼미션을 설정해줘야함.
 *          => Manifest 파일에 permission 태그를 등록해줘야 함 => 해당 컴포넌트 (자신의 컴포넌트)를 보호
 *
 * 이때, 외부에서 이 컴포넌트를 사용하려는 어플리케이션 내에서는 당연히, 코드적으로 완벽한 준비가 되어있어야 하고,
 *      여기에 + @ 자신의 매니페스트 파일에다가 그 퍼미션을 이용하겠다는 뜻으로 uses-permission 태그를 달아줘야 함.
 *
 *      그래서 이런 설정 개념이 퍼미션의 원론적인 얘기다.
 *
 */


/**
 * Permission
 *
 * <permission>
 * AndroidManifest.xml 파일 설정
 * - name : permission의 이름
 * - label, description : permission 에 대한 설명
 * - protectionLevel : 보호 수준
 *      normal : 낮은 수준의 보호. 유저에게 권한 부여 요청이 필요 없는 경우
 *      dangerous : 높은 수준의 보호. 유저에게 권한 부여 요청이 필요한 경우
 *      signature : 동일한 키로 사인된 앱만 실행
 *      signatureOrSystem : 안드로이드 시스템 앱이거나 동일키로 사인된 앱만 실행
 *
 *  퍼미션태그만 써주고 끝인게 아니라
 *  보호하고자 하는 컴포넌트의 속성으로 permission을 작성해줘야 보호가 됨
 *
 *
 *
 *
 *  이용하는 쪽
 * <uses-permission>
 * AndroidManifest.xml 설정
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 *
 *
 * 이것들에 대한 확실한 사례 => 설정에서의 앱 권한 -> 즉, 앱 권한 설정
 *
 *
 *
 * 시스템에서 요구되는 퍼미션
 *
 * - ACCESS_FINE_LOCATION : 위치 정도 접근
 * - ACCESS_NETWORK_STATE : 네트워크 정보 접근
 * - ACCESS_WIFI_STATE : Wi-Fi 네트워크 정보 접근
 * - BATTERY_STATS : 배터리 정보 접근
 * - BLUETOOTH : 블루투스 장치에 연결
 * - BLUETOOTH_ADMIN : 블루투스 장치를 검색하고 페어링
 * - CAMERA : 카메라 장치에 접근
 * - INTERNET : 네트워크 연결
 *
 * - READ_EXTERNAL_STORAGE : 외부 저장소에서 파일 읽기
 * - WRITE_EXTERNAL_STORAGE : 외부 저장소에서 파일 쓰기
 * - READ_PHONE_STATE : 전화기로서의 각종 정보 접근
 * - SEND_SMS : SMS 발신
 * - RECEIVE_SMS : SMS 수신
 * - RECEIVE_BOOT_COMPLETED : 부팅 완료시 실행
 * - VIBRATE : 진동 울리기
 *
 * <<전반적으로 액세스 권한 관련 이야기 인듯>>
 *     API 23 버전부터는 허가제로 바뀌었다고 함. (생각해보니까 그러네, 언제부턴가 마이크 카메라 이런거 허가해야 쓸수있었음)
 *
 *
 */


/**
 * [permission check]
 *
 * permission 이 유저가 체크할 수 있는거기 때문에 이런거 고려해서 프로그램 짜야하며
 * 이게 뭐 코드에서 enable 로 조정해서 프로그램작성하고 그런게 안됨. 유저만 건들수 있는거기때문에
 *
 * 퍼미션 체크는 checkSelfPermission() 함수 이용
 * 결과 값은 상수로 전달
 * PackageManager.PERMISSION_GRANTED : 퍼미션이 허락된 경우
 * PackageManager.PERMISSION_DENIED : 퍼미션이 거부된 경우
 *
 * <sample code>
 *     val status = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")
 *     if(status = PackageManager.PERMISSION_GRANTED) {
 *          Log.d("kkang", "permission granted")
 *     } else {
 *          Log.d("kkang", "permission denied")
 *     }
 *
 *
 */


/**
 * [permission request]
 *
 * <requestPermission() 방법>
 *
 * ActivityCompat.requestPermissions(this, arrayOf<String>("android.permission.ACCESS_FINE_LOCATION"), 100)
 *
 * override fun onRequestPermissionsResult(
 *      requestCode: Int,
 *      permissions: Array<out String>,
 *      grantResults: IntArray
 * ) {
 *      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
 *      if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
 *          Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
 *      } else {
 *          Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
 *      }
 *  }
 *
 *
 *
 *
 *
 *  <registerForActivityResult() 방법>
 *
 *  val requestPermissionLauncher = registerForActivityResult(
 *      ActivityResultContracts.RequestPermission()
 *  ) { isGranted ->
 *      if(isGranted) {
 *          Toast.makeText(this, "permission granted2", Toast.LENGTH_SHORT).show()
 *      } else {
 *          Toast.makeText(this, "permission denied2", Toast.LENGTH_SHORT).show()
 *      }
 *  }
 *
 *  requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
 *
 *
 *
 */