package com.misw.appvynills

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Configura el layout que crearás

        // Espera de 3 segundos antes de redirigir
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra el SplashActivity para que no se regrese a esta pantalla
        }, 3000) // Cambia a 5000 para 5 segundos si prefieres
    }
}