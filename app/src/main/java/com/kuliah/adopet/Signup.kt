package com.kuliah.adopet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupBtn.setOnClickListener {
//            val status = databaseHandler.loginAccount(email.text.toString(), password.text.toString())
//            if (status) {
            finish()
//            } else {
//                Toast.makeText(getApplicationContext(), "Pastikan username dan password anda benar", Toast.LENGTH_SHORT).show();
//            }
        }

        loginBtn.setOnClickListener{
            finish()
        }
    }
}