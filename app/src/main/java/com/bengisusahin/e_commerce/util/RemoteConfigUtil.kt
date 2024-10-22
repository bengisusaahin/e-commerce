package com.bengisusahin.e_commerce.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

// RemoteConfigUtil class to manage remote config operations
@Singleton
class RemoteConfigUtil @Inject constructor() {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    val colorLiveData = MutableLiveData<String>()

    init {
        // Set default values
        remoteConfig.setDefaultsAsync(mapOf("backgroundColor" to "#FFFFFF"))

        // Set minimum fetch interval
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Add listener to update the colorLiveData when remote config is updated
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.e("onUpdate", "Updated keys: " + configUpdate.updatedKeys)

                if (configUpdate.updatedKeys.contains("backgroundColor")) {
                    remoteConfig.activate().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val color = FirebaseRemoteConfig.getInstance().getString("backgroundColor")
                            Log.d("Pull Color", color)
                            colorLiveData.value = color
                        }
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w("onError", "Config update error with code: " + error.code, error)
            }
        })
    }

    // Fetch remote config and update the colorLiveData
    fun fetchRemoteConfig(callback: (Boolean, String?) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("RemoteConfigUtil", "Config params updated: $updated")
                    if (updated) {
                        val color = remoteConfig.getString("backgroundColor")
                        Log.d("RemoteConfigUtil", "Pulled Color: $color")
                        colorLiveData.value = color
                    }else {
                        callback(true, null)
                    }
                } else {
                    Log.d("RemoteConfigUtil", "Config fetch failed")
                    callback(false, null)
                }
            }
    }

    fun getBackgroundColor(): String {
        return remoteConfig.getString("backgroundColor")
    }
}
