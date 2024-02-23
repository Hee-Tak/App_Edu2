package com.tak.c80

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity2 : AppCompatActivity() {

    lateinit var resultView: TextView
    lateinit var manager: LocationManager
    lateinit var locationListener: LocationListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        resultView = findViewById(R.id.resultView)
        manager = getSystemService(LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                updateLocationInfo(location)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
                // Provider가 활성화되었을 때의 동작 (필요에 따라 구현)
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
                // Provider가 비활성화되었을 때의 동작 (필요에 따라 구현)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
                // Provider 상태 변경 시의 동작 (필요에 따라 구현)
            }
        }


        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted ->
            if(isGranted){
                startLocationUpdates()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }



        val status = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if(status == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates()
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }




        val back = findViewById<Button>(R.id.backButton)
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun startLocationUpdates() {
        manager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            locationListener
        )
    }

    private fun updateLocationInfo(location: Location) {
        val latitude = location.latitude //위도
        val longitude = location.longitude //경도
        val accuracy = location.accuracy
        val time = location.time

        resultView.text = " 위도: $latitude \n 경도: $longitude \n 오차범위: $accuracy \n 시각: $time \t location: $location"
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.removeUpdates(locationListener)
    }
}