package com.example.autocallrejector.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.autocallrejector.R

class TonePlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playUnavailableMessage() {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.unavailable_message)
            mediaPlayer?.let { player ->
                if (player.isPlaying) player.stop()
                player.setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                }
                player.setVolume(1.0f, 1.0f)
                player.start()
            } ?: Log.w("TonePlayer", "unavailable_message.mp3 not found; skipping")
        } catch (e: Exception) {
            Log.e("TonePlayer", "Error playing unavailable message", e)
            release()
        }
    }

    private fun release() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }
}