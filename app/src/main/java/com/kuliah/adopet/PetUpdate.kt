package com.kuliah.adopet

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kuliah.adopet.database.DatabaseHandler
import com.kuliah.adopet.databinding.ActivityPetCrudBinding
import com.kuliah.adopet.model.PetModelClass
import kotlinx.android.synthetic.main.activity_pet_crud.*
import kotlinx.android.synthetic.main.activity_pet_update.*
import kotlinx.android.synthetic.main.activity_pet_update.Female
import kotlinx.android.synthetic.main.activity_pet_update.L
import kotlinx.android.synthetic.main.activity_pet_update.M
import kotlinx.android.synthetic.main.activity_pet_update.Male
import kotlinx.android.synthetic.main.activity_pet_update.S
import kotlinx.android.synthetic.main.activity_pet_update.address
import kotlinx.android.synthetic.main.activity_pet_update.descri
import kotlinx.android.synthetic.main.activity_pet_update.fullname
import kotlinx.android.synthetic.main.activity_pet_update.latitude
import kotlinx.android.synthetic.main.activity_pet_update.longitude
import kotlinx.android.synthetic.main.activity_pet_update.pet_ag
import kotlinx.android.synthetic.main.activity_pet_update.pet_we
import kotlinx.android.synthetic.main.activity_pet_update.vaksin_no
import kotlinx.android.synthetic.main.activity_pet_update.vaksin_yes
import java.util.*

class PetUpdate : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_update)

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val logedID = databaseHandler.checkAccount()
        val ID: String? = intent.extras?.getString("ID")

        val binding = ActivityPetCrudBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayList_cat = arrayListOf<String>("Persia", "Bengal", "Siberia")
        val arrayList_dog = arrayListOf<String>("Bulldog", "Pudel", "Husky")

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this!!)

        val gpsButtonn = findViewById<TextView>(R.id.gpsBtn_update)
        gpsBtn_update.setOnClickListener {
            Toast.makeText(this, "Mendapatkan lokasi terkini", Toast.LENGTH_SHORT).show();
            getLastKnownLocation()
        }
        var category:String = ""

        catBtn_update.setOnClickListener {
            catBtn_update.setBackgroundResource(R.drawable.btn_secondary)
            dogBtn_update.setBackgroundResource(R.drawable.btn_tertiary)
            category = "Cat"
            binding.pettype.adapter = ArrayAdapter(applicationContext, R.layout.spinner_text, arrayList_cat)
        }
        dogBtn_update.setOnClickListener {
            catBtn_update.setBackgroundResource(R.drawable.btn_tertiary)
            dogBtn_update.setBackgroundResource(R.drawable.btn_secondary)
            category = "Dog"
            binding.pettype.adapter = ArrayAdapter(applicationContext, R.layout.spinner_text, arrayList_dog)
        }

        var emp: List<PetModelClass> = databaseHandler.getPet("ID", ID!!)

        fullname.setText(emp[0].name)
        address.setText(emp[0].address)
        latitude.setText(emp[0].x)
        longitude.setText(emp[0].y)
        pet_ag.setText(emp[0].age)
        pet_we.setText(emp[0].weight)
        descri.setText(emp[0].desc)

        when (emp[0].category) {
            "Cat" ->  {
                catBtn_update.setBackgroundResource(R.drawable.btn_secondary)
                binding.pettype.adapter = ArrayAdapter(applicationContext, R.layout.spinner_text, arrayList_cat)
                category = "Cat"
            }
            "Dog" -> {
                dogBtn_update.setBackgroundResource(R.drawable.btn_secondary)
                binding.pettype.adapter = ArrayAdapter(applicationContext, R.layout.spinner_text, arrayList_dog)
                category = "Dog"
            }
        }
        when (emp[0].sex) {
            "Male" -> Male.isChecked = true
            "Female" -> Female.isChecked = true
        }
        when (emp[0].size) {
            "S" -> S.isChecked = true
            "M" -> M.isChecked = true
            "L" -> L.isChecked = true
        }
        when (emp[0].vaccine) {
            "Yes" -> vaksin_yes.isChecked = true
            "No" -> vaksin_no.isChecked = true
        }
        when (emp[0].type) {
            "Persia" -> pettype_update.setSelection(0)
            "Bengal" -> pettype_update.setSelection(1)
            "Siberia" -> pettype_update.setSelection(2)
            "Bulldog" -> pettype_update.setSelection(0)
            "Pudel" -> pettype_update.setSelection(1)
            "Husky" -> pettype_update.setSelection(2)
        }

        backBtn_update.setOnClickListener {
            finish()
        }
        cancel_button_update.setOnClickListener {
            finish()
        }
    }

    private fun getLastKnownLocation() {

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