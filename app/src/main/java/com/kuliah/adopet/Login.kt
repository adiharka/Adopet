package com.kuliah.adopet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {
//            val status = databaseHandler.loginAccount(email.text.toString(), password.text.toString())
//            if (status) {
                val intent = Intent(this, MainActivity::class.java)
                Toast.makeText(getApplicationContext(), "Sukses login", Toast.LENGTH_SHORT).show();
                startActivity(intent)
                finish()
//            } else {
//                Toast.makeText(getApplicationContext(), "Pastikan username dan password anda benar", Toast.LENGTH_SHORT).show();
//            }
        }

        signupBtn.setOnClickListener{
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}