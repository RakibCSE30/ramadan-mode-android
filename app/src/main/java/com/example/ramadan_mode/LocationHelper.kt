package com.example.ramadan_mode

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * এই ক্লাসটা ইউজারের বর্তমান লোকেশন (lat/long) বের করে আনে।
 * Sehri/Iftar সময় ক্যালকুলেট করতে এই lat/long দরকার হবে।
 */
class LocationHelper(private val context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    // পারমিশন দেওয়া আছে কিনা চেক করে
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // বর্তমান লোকেশন এনে দেয় (suspend function - coroutine এর মধ্যে কল করতে হবে)
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Pair<Double, Double>? {
        if (!hasLocationPermission()) return null

        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(Pair(location.latitude, location.longitude))
                    } else {
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }
}