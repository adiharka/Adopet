package com.kuliah.adopet

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_profile_update.*
import android.location.Geocoder
import android.util.Log
import kotlinx.android.synthetic.main.activity_pet_detail.*
import kotlinx.android.synthetic.main.activity_profile_update.backBtn
import java.util.*

class ProfileUpdate : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this!!)

        gpsBtn.setOnClickListener {
            getLastKnownLocation()
        }
        backBtn.setOnClickListener {
            finish()
        }

    }

    fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location->
                if (location != null) {
                    latitude.setText(location.latitude.toString())
                    longitude.setText(location.longitude.toString())

                    var addresses: List<Address>
                    val geocoder: Geocoder
                    geocoder = Geocoder(this, Locale.getDefault())
                    addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )

                    val addressValue = addresses[0].getFeatureName() + ", " + addresses[0].getLocality() + ", " + addresses[0].getAdminArea() + ", " + addresses[0].getCountryName() + " " + addresses[0].getPostalCode()
                    address.setText(addressValue)

                }

            }

    }
}