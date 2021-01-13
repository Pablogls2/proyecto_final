package com.example.tenisclubdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.tenisclubdroid.ui.Login.LoginActivity


class SplashScreen : AppCompatActivity() {
    lateinit var handler:Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //dejamos 3 segundos para cargar el splash screen
        handler= Handler()
        handler.postDelayed({

                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
        }, 3000)


    }
}