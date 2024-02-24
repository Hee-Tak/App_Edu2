package com.tak.c83

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


//지도라는게 콜백으로 전달된다. 그래서 인터페이스를 이쪽에서 구현한다면서 아래에 AppCompatActivity() 부분 옆에 추가 OnMapReadyCallback 추가
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    var googleMap: GoogleMap? = null    //지도 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment)!!.getMapAsync(this)   // 콜백함수가 현 클래스에 이렇게 등록함. 그러면 이제 콜백 함수를 오버라이드 받아서 작성해놓으면 되겠지. 그게 onMapReady
    }

    override fun onMapReady(p0: GoogleMap) {
        //이 함수가 호출이 되었다라는 것은 지도 객체를 우리 앱에서 이용이 가능하다는 얘기. 그래서 이 함수의 매개변수가 지도 객체가 되겠고
        googleMap = p0
        val latLng = LatLng(37.566610, 126.978403) //임의로 지정. (서울 시청값이란다) (서울 시청을 센터로 잡기 위한 위경도 값)
        val position = CameraPosition.Builder() //카메라라는 표현을 썼지만, 지도화면이라 보면되고, 지도 화면을 설정하기 위한 정보 라고 보면된다.
            .target(latLng)     //센터위치설정
            .zoom(16f)             //초기 지도가 뜨는 줌 레벨
            .build()                //여기까지 준비한건 정보. 이제 이 정보대로 지도에 센터가 보이게끔 명령을 내려야함.

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position)) //이렇게 하면, 우리가 원하는 위치를 센터로 해서 지도를 뿌릴 수가 있다.
    }
}
//===============================================================================================================================================================================================
        //코드는 여기까지인데, 마지막으로 API_KEY 등록까지 해야함. (매니페스트 파일에서) -> 구글 개발자 사이트에서 PC에서 SHA1 지문을 얻어야됨 -> 이 지문을 어플리케이션 사인했던 키값으로 얻어내면 된다.
        //근데 테스트용으로는 안드로이드에서 그냥 클릭 클릭해서 된다고 함
        //일단 이 작업 자체는 File->Settings->(맨마지막부분인)Experimental->Gradle의 Do not build Gradle task list during Gradle sync 체크박스 해제 -> 그런다음에, 다시 gradle sync
        //그다음 안드로이드 스튜디오 좌측측면 탭에 Gradle -> 해당 모듈 -> Tasks -> android -> signingReport 에 가야하는데 지금 난, android 가 없음. 다른 방법을 알아봐야함.

        //근데 22.3.1 패치 이후에는 Configure all Gradle tasks during Gradle sync 얘를 체크하고 해야한다는거 같음. (Do not build Gradle task list during Gradle sync 해제 대신에.)

        //위와 같이 하거나, Android Studio 터미널에서, ./gradlew tasks --all 명령어를 실행하여 해결할 수도 있다고 함.

        //아무튼! 이게 지금 signingReport 클릭해서 키툴이다 라고 하는것을 통해서 얻어낸 SHA1 지문이다.
        //이 SHA1 지문이 있어야, 구글 맵 키를 얻어낼 수 있다. ..(테스트용으론 이렇게간단하게 얻어낼 수 있음!)

        //근데 정식 앱이었다면, 어플리케이션 빌드 하면서 싸인했던 키 가지고 얻어줘야 한다.




        // 아무튼 이제 구글 개발자 사이트 이동하려면, 이게 매번 URL이 바뀌기도 해서. 구글 검색으로 가셈 -> google 검색 : "android map console" => 그럼 대충 https://developers.google.com/maps/documentation/android-sdk/get-api-key?hl=ko 이런 곳 나옴
        // 진입하고 -> 시작하기
        // 초기상태면 이것저것 설정한 뒤,
        // 왼쪽 탭에 API 및 서비스 -> 라이브러리 OR 사용자 인증 정보    얘네만 보면됨. 지도 API만 가져다 쓴다라고 한다면
        // 라이브러리에서 -> Maps SDK for Android -> 사용
        // 다시 돌아와서 api 서비스 -> 사용자 인증 정보 -> 위쪽에 + 사용자 인증 정보 만들기 -> API 키 -> 키 제한
        // 그럼, 하나의 키가 생성되었고 이 키를 등록하는건 맞긴 맞는데, 이걸 안드로이드 앱에서 사용하려면 키 제한을 걸어야 한다. (근데 나는 지금 키 제한이 안됨. 왜지? 무료맨이라 그런가)
        // 그럼 이제 API 키 눌러서 키 제한사항 -> 앱 제한 설정 -> Android 앱 //API 제한사항 -> 키 제한 -> Select APIs -> Maps SDK for Android 체크
        // 그다음, Android 제한사항 +(추가) -> 안드로이드 앱 추가에 패키지이름, SHA-1 인증서 디지털 지문 넣고 저장 => 사용 가능한 상태가 된다!
        // 그럼 이제, API key 를 복사해서 Manifest 파일에 api 키 등록하는 곳에 복붙.

//===============================================================================================================================================================================================
/**
 *
 * c83 ) GoogleMap
 *
 * * 위치 정보를 가져온 다음에, 위경도 값을 내부적으로만 이용할 수 도 있지만, 대부분의 경우 지도랑 많이 연동할 수 있다. -> 지도 띄우는 방법을 알자!
 * * 지도와 관련되어있는 것 -> 특정 벤더의 컨텐츠.
 * * 안드로이드 앱을 개발하겟다라고 하면, 일차적으로, 어느 벤더의 콘텐츠를 이용할 지를 결정해줘야 한다.
 * * 안드로이드 앱을 만든다라고 해서 꼭 구글 지도를 이용할 필요는 없다. 다른 벤더에서 제공되고 있는 지도를 이용해도 상관은 없다.
 *
 *
 * * 지도를 이용하겠다라고 하면, 별도의 api를 이용해줘야 한다.
 *
 * <AndroidManifest.xml 설정>
 *
 *          implementation 'com.google.android.gms:play-services:12.0.1'                    // (이전 시간에서도 별도로 dependency로 설정해준 play-services api) -> 여기에서 지도를 핸들링할 수 있는 api를 제공한다. 그래서 dependency 관계 설정해줘야함.
 *
 *          // 지도 띄우기 위해 필요한 퍼미션들.
 *          <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>       // 네트워킹을 하기 전에, 네트워크 상태 파악을 위해서 필요한 퍼미션. 직접 구현하는건 아니지만, 내부적으로 우리가 쓰는 API가 네트워크 상태 파악을 하기 때문에 달아줘야함.
 *          <uses-permission android:name="android.permission.INTERNET"/>                   // 안드로이드 개발자에게 아주 유명한 퍼미션. 외부 서버랑 네트워킹할 때 필요하다. 여기서는 직접은아니지만, google 서버에 등록되어있는 map 을 끌어오기 위해 사용 (구글 서버와의 네트워킹)
 *          <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>     // 외장 메모리 공간에 파일을 write 할 때, 필요한 퍼미션. 우리가 하진 않지만, 내부적으로 이 api에서 구글 서버에서 받은 지도 데이터를 캐싱 차원에서 파일로 write 하기 때문에, 필요한 퍼미션.
 *
 *
 *          // 추가적인 Manifest 파일 설정
 *          <uses-library android:name="org.apache.http.legacy" android:required="false"/>  // 아파치 라이브러리를 쓰겠다. 선언 => 얘를 이용해서 많은 부분에서 네트워크 프로그램을 작성. 플레이서비스 버전에 따라서 여전히 아파치 라이브러리를 쓰고 있는 곳이 있을 수도 있다.
 *          <meta-data android:name="com.google.android.maps.v2.API_KEY"                    //메타 정보를 매니페스트 파일에 선언   (이 부분 자체는 맵키가 등록되는 부분)
 *              android:value="~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"/>
 *          <meta-data android:name="com.google.android.gms.version"                        //몇버전의 라이브러리를 쓰는지.
 *              android:value="@integer/google_play_services_version"/>
 *
 *          // 그리고, 지도는 벤더의 컨텐츠로, 그냥 넘겨줄 리가 없다.
 *          // 그래서 그 벤더의 개발자 콘솔, 즉, 개발자 사이트에 가서 맵키(API_KEY)를 얻어내야 한다.
 *          // 이 얻어진 키를 우리 앱에서 정상적으로 가지고 있어야 한다. 그래야지 정상적으로 지도 데이터가 넘어온다.
 *
 *
 * * 지도도 화면이다. 화면을 구성하기 위해서는 UI를 구현하기 위한 뷰 View 가 필요하다.
 *
 * <GoogleMap>
 *
 * - play-service 라이브러리에서 지도는 프레그먼트로 제공         (이 지도 View 는, Fragment 로 제공한다.) (우리가 직접 작성한 프레그먼트는 아님) (api에서 제공해주는 프레그먼트)
 *
 *          <fragment
 *              xmlns:android="http://schemas.android.com/apk/res/android"
 *              android:id="@+id/mapView"
 *              android:layout_width="match_parent"
 *              android:layout_height="match_parent"
 *              android:name="com.google.android.gms.maps.SupportMapFragment"       //api에서 제공해주고 있는 프레그먼트
 *              />
 *
 *
 * * 이제 지도를 얻는 방법! -> 콜백을 통해서 전달한다.
 *
 * - GoogleMap 이라는 클래스가 지도를 출력
 *
 *          override fun onCreate(savedInstanceState: Bundle?) {
 *              super.onCreate(savedInstanceState)
 *              setContentView(R.layout.activity_main)
 *
 *              (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?)!!.getMapAsync(this)              //R.id.mapView : 아까 지도를 등록한 프레그먼트... 여기에다가
 *          }                                                                                                                   //getMapAsync 라고 등록을 해주면, 어느 순간에 지도가 이용 가능한 상태가 됐을 때 콜백을 해준다.
 *                                                                                                                              //이 콜백이, 여기에 onMapReadt 라는 함수가 되겠다.
 *
 *          override fun onMapReady(p0: GoogleMap?) {                                                                           //얘 ㅇㅇ
 *              googleMap = p0                                                                                                  //콜백을 등록하고 지도 가능한 상태로 됐을 때
 *                                                                                                                              //이 콜백이 콜되면서 매개변수로 전달된다. (p0: GoogleMap?)
 *          }
 *
 *          //결과적으로 이 매개변수로 전달되는 GoogleMap 이라는 객체가, 지도 객체라고 보면 된다.
 *
 *          //실습에서, 위경도 설정, 지도의 센터위치 설정, 맵키 얻는 방법까지 정상적으로 따라해야 됨.
 *
 *
 *
 */


/**
 * 결국에 파일들을 체크할 때,
 * Manifest 파일, Activity 등의 컴포넌트파일들, xml 파일, build.gradle 까지 확인해주는게 좋다는거지
 */