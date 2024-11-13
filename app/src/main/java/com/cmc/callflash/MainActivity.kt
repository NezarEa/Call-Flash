package com.cmc.callflash

import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var switchFlash: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchFlash = findViewById(R.id.switchFlash)

        switchFlash.isChecked = getFlashState()

        switchFlash.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startFlashService()
            } else {
                stopFlashService()
            }
        }
    }

    private fun startFlashService() {
        val intent = Intent(this, FlashOnCallService::class.java)
        startService(intent)
        saveFlashState(true)
    }

    private fun stopFlashService() {
        val intent = Intent(this, FlashOnCallService::class.java)
        stopService(intent)
        saveFlashState(false)
    }

    private fun saveFlashState(isEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences("flash_settings", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("flash_enabled", isEnabled).apply()
    }

    private fun getFlashState(): Boolean {
        val sharedPreferences = getSharedPreferences("flash_settings", MODE_PRIVATE)
        return sharedPreferences.getBoolean("flash_enabled", false)
    }
}
