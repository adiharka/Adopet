package com.kuliah.adopet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pet_detail.*

class PetDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_detail)

        btnSecondary.setOnClickListener {
            val intent = Intent(this, Map::class.java)
            startActivity(intent)
        }

        backBtn.setOnClickListener {
            finish()
        }
    }
}