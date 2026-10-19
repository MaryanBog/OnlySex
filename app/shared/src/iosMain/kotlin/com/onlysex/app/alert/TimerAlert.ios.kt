package com.onlysex.app.alert

import platform.AudioToolbox.AudioServicesPlaySystemSound
import platform.AudioToolbox.kSystemSoundID_Vibrate

actual fun playTimerAlert() {
    AudioServicesPlaySystemSound(1005u)
    AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
}