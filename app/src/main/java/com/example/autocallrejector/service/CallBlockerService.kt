package com.example.autocallrejector.service

import android.telecom.Call
import android.telecom.CallScreeningService
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q) // This service is for Android 10 (API 29) and higher
class CallBlockerService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val incomingNumber = callDetails.getHandle().schemeSpecificPart
        
        // This is where your core logic goes.
        // You need to check if 'incomingNumber' is in your blocklist.
        val shouldBlock = isNumberInBlocklist(incomingNumber) // You must implement this function

        val response = if (shouldBlock) {
            // Build the response to reject the call
            CallResponse.Builder()
                .setDisallowCall(true)  // Disallow the call from ringing
                .setRejectCall(true)    // Officially reject it
                .setSkipCallLog(false)  // Keep a record in the call log
                .setSkipNotification(false) // Show a "blocked call" notification
                .build()
        } else {
            // If not in the blocklist, allow the call
            CallResponse.Builder().build()
        }
        
        respondToCall(callDetails, response)
    }

    private fun isNumberInBlocklist(number: String): Boolean {
        // TODO: Implement this logic.
        // For example, read from SharedPreferences, a database, or a file
        // where you've saved the numbers to be blocked.
        // For now, a simple example:
        val blockedNumbers = setOf("1234567890", "9876543210") // Replace with your dynamic list
        return blockedNumbers.contains(number)
    }
}