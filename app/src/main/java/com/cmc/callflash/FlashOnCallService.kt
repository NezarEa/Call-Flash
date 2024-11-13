package com.cmc.callflash

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.telephony.TelephonyManager

class FlashOnCallService : Service() {

    private lateinit var cameraManager: CameraManager
    private val handler = Handler(Looper.getMainLooper())
    private var isFlashing = false
    private val flashInterval = 500L

    override fun onCreate() {
        super.onCreate()
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        val telephonyReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                    TelephonyManager.EXTRA_STATE_RINGING -> startFlashing()
                    TelephonyManager.EXTRA_STATE_OFFHOOK, TelephonyManager.EXTRA_STATE_IDLE -> stopFlashing()
                }
            }
        }
        registerReceiver(telephonyReceiver, IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED))
    }

    private fun startFlashing() {
        isFlashing = true
        flashOnOffRunnable.run()
    }

    private fun stopFlashing() {
        isFlashing = false
        handler.removeCallbacks(flashOnOffRunnable)
        setFlash(false)
    }

    private val flashOnOffRunnable = object : Runnable {
        override fun run() {
            setFlash(isFlashing)
            isFlashing = !isFlashing
            handler.postDelayed(this, flashInterval)
        }
    }

    private fun setFlash(state: Boolean) {
        try {
            cameraManager.cameraIdList.firstOrNull()?.let {
                cameraManager.setTorchMode(it, state)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
