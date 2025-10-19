package com.example.autocallrejector.service

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.autocallrejector.AutoCallRejectorApplication
import com.example.autocallrejector.data.BlockedNumberRepository
import com.example.autocallrejector.utils.NotificationHelper
import com.example.autocallrejector.utils.TonePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
class CallScreeningService : CallScreeningService() {
    // Lazy init: Get repository from global Application DB
    private val repository: BlockedNumberRepository by lazy {
        val app = application as AutoCallRejectorApplication
        BlockedNumberRepository(app.database.blockedNumberDao())
    }
    private val notificationHelper = NotificationHelper(this)
    private val tonePlayer = TonePlayer(this)

    // Main callback: Triggered for each incoming/outgoing call
    override fun onScreenCall(callDetails: Call.Details) {
        // Extract phone number from handle (e.g., "tel:1234567890" -> "1234567890")
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: run {
            Log.w("CallScreeningService", "No phone number in call details")
            return  // Skip if no number
        }

        // Only screen incoming calls (ignore outgoing)
        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) {
            respondToCall(callDetails, CallScreeningService.CallResponse.Builder().build()) // Allow non-incoming
            return
        }

        // Async check if blocked (use IO dispatcher for DB query)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val isBlocked = repository.isBlocked(phoneNumber)
                launch(Dispatchers.Main) {  // Switch to Main for UI/service ops
                    if (isBlocked) {
                        // Reject: DISALLOW makes caller hear busy/unavailable tone (no ring on device)
                        val response = CallScreeningService.CallResponse.Builder()
                            .setDisallowCall(true)  // Reject call
                            .setRejectCall(true)    // Ensure rejection
                            .setSkipCallLog(false)  // Optional: Log the call
                            .setSkipNotification(true)  // No default notification
                            .build()
                        respondToCall(callDetails, response)

                        // Show custom notification to user
                        notificationHelper.showBlockedCallNotification(phoneNumber)

                        // Play unavailable message to the caller
                        tonePlayer.playUnavailableMessage()
                    } else {
                        // Allow non-blocked calls
                        respondToCall(callDetails, CallScreeningService.CallResponse.Builder().build())
                    }
                }
            } catch (e: Exception) {
                Log.e("CallScreeningService", "Error checking blocked number", e)
                // Fallback: Allow call on error
                launch(Dispatchers.Main) {
                    respondToCall(callDetails, CallScreeningService.CallResponse.Builder().build())
                }
            }
        }
    }
}
