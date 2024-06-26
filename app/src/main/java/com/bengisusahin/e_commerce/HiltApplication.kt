package com.bengisusahin.e_commerce

import android.app.Application
import android.util.Log
import com.bengisusahin.e_commerce.util.RemoteConfigUtil
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

// HiltApplication class is annotated with @HiltAndroidApp to generate the Hilt components
@HiltAndroidApp
class HiltApplication : Application(){
    @Inject
    lateinit var remoteConfigUtil: RemoteConfigUtil

    override fun onCreate() {
        super.onCreate()
        remoteConfigUtil.fetchRemoteConfig {success, color ->
            if (success) {
                // Remote config fetched successfully
                Log.d("MyApplication", "Remote config fetched successfully")
                if (color != null) {
                    Log.d("MyApplication", "Initial background color: $color")
                }
            } else {
                // Remote config fetch failed
                Log.d("MyApplication", "Failed to fetch remote config")
            }
        }
    }
}