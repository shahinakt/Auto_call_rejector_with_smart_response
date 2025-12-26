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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.Q)
class AppCallScreeningService : CallScreeningService() {
    private val repository: BlockedNumberRepository by lazy {
        val app = application as AutoCallRejectorApplication
        BlockedNumberRepository(app.database.blockedNumberDao())
    }
    private val notificationHelper = NotificationHelper(this)
    private val tonePlayer = TonePlayer(this)
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: run {
            Log.w("CallScreeningService", "No phone number in call details")
            return
        }

        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) {
            respondToCall(callDetails, CallScreeningService.CallResponse.Builder().build())
            return
        }

        serviceScope.launch {
            try {
                val isBlocked = repository.isBlocked(phoneNumber)
                withContext(Dispatchers.Main) {
                    if (isBlocked) {
                        val response = CallScreeningService.CallResponse.Builder()
                            .setDisallowCall(true)
                            .setRejectCall(true)
                            .setSkipCallLog(false)
                            .setSkipNotification(true)
                            .build()
                        respondToCall(callDetails, response)
                        notificationHelper.showBlockedCallNotification(phoneNumber)
                        tonePlayer.playUnavailableMessage()
                    } else {
                        respondToCall(callDetails, CallScreeningService.CallResponse.Builder().build())
                    }
                }
            } catch (e: Exception) {
                Log.e("CallScreeningService", "Error checking blocked number", e)
                withContext(Dispatchers.Main) {
                    respondToCall(callDetails, CallScreeningService.CallResponse.Builder().build())
                }
            }
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}
