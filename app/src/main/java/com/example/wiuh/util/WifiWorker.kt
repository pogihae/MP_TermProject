package com.example.wiuh.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.wiuh.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CountDownLatch

const val KEY_USER = "USER"

class WifiWorker(context: Context, parameters: WorkerParameters) :
    Worker(context, parameters) {

    private val latch = CountDownLatch(1)

    private val networkCallback = object :
        ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            val wifiManager = context.applicationContext
                .getSystemService(Context.WIFI_SERVICE) as WifiManager

            val wifiName = wifiManager.bssid()
            val uId = inputData.getString(KEY_USER)

            var unReadNum: Long

            FirebaseUtil
                .getUserRef().child(wifiName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        unReadNum = snapshot.childrenCount
                        NotificationUtil(context)
                            .createNotification("SSID", unReadNum.toString())
                        latch.countDown()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        latch.countDown()
                    }
                })
        }
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    override fun doWork(): Result {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
            Log.w(
                "WifiWorker",
                "NetworkCallback for Wi-fi was not registered or already unregistered"
            )
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        latch.await()
        return Result.success()
    }

    fun WifiManager.bssid(): String = connectionInfo.run {
        if (this.ssid.contains("<unknown ssid>")) "UNKOWN" else this.bssid
    }
}