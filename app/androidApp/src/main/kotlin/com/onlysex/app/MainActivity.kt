package com.onlysex.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.onlysex.app.alert.initializeTimerAlert

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        initializeTimerAlert(applicationContext)

        enableEdgeToEdge()

        setContent {
            App()
        }
    }
}