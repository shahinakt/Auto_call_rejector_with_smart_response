package com.example.autocallrejector.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.autocallrejector.R

class TonePlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    // Play the optional tone: Load from res/raw/block_tone.mp3, play once, release
    fun playTone() {
        // Optional: Add toggle via SharedPreferences here (e.g., if (prefs.getBoolean("tone_enabled", true)))
        // For minimalism, always play if file exists

        try {
            // Create new MediaPlayer instance (reset if reusing)
            mediaPlayer = MediaPlayer.create(context, R.raw.block_tone)  // Load raw resource
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()  // Stop any prior play
                }
                player.setOnCompletionListener {
                    it.release()  // Auto-release after play (good practice)
                    mediaPlayer = null
                }
                player.setVolume(0.5f, 0.5f)  // Low volume for alert (left/right channels)
                player.start()  // Play the tone (device-local only)
            } ?: run {
                Log.w("TonePlayer", "block_tone.mp3 not found in res/raw; skipping tone")
                // Silent fail: No tone if file missing (no crash)
            }
        } catch (e: Exception) {
            Log.e("TonePlayer", "Error playing tone", e)
            mediaPlayer?.release()
            mediaPlayer = null
            // Graceful: Continue without tone
        }
    }

    fun playUnavailableMessage() {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.unavailable_message)
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                }
                player.setVolume(1.0f, 1.0f) // Full volume for the message
                player.start()
            } ?: run {
                Log.w("TonePlayer", "unavailable_message.mp3 not found in res/raw; skipping message")
            }
        } catch (e: Exception) {
            Log.e("TonePlayer", "Error playing unavailable message", e)
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    // Optional: Stop if needed (e.g., on app close)
    fun stopTone() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }
}