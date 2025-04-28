package io.engst.bimbam.model

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator

class AcousticFeedback(private val context: Context) {
    private var ringtone: Ringtone? = null

    fun play() {
        val vibrator = context.getSystemService(Vibrator::class.java)
        val timings: LongArray = longArrayOf(50, 50, 50, 50, 50, 100, 350, 25, 25, 25, 25, 200)
        val amplitudes: IntArray = intArrayOf(33, 51, 75, 113, 170, 255, 0, 38, 62, 100, 160, 255)
        val repeatIndex = 0
        vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex))

        ringtone =
            RingtoneManager.getRingtone(
                context,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            )
                .apply {
                    isLooping = true
                    play()
                }
    }

    fun stop() {
        context.getSystemService(Vibrator::class.java).cancel()
        ringtone?.stop()
        ringtone = null
    }
}