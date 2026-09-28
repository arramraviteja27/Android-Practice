package com.design.practice

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.design.practice.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PracticeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d("APP_CHECK", "Application onCreate called")
        Log.d("APP_CHECK", "DEBUG = ${BuildConfig.DEBUG}")

        Firebase.initialize(context = this)

        if (BuildConfig.DEBUG) {

            Log.d("APP_CHECK", "Installing Debug App Check provider")
            Firebase.appCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )

            Firebase.appCheck
                .getAppCheckToken(false)
                .addOnSuccessListener { result ->
                    Log.d(
                        "APP_CHECK",
                        "TOKEN: ${result.token}"
                    )
                }
                .addOnFailureListener { exception ->
                    Log.e(
                        "APP_CHECK",
                        "ERROR: ${exception.message}",
                        exception
                    )
                }
        }

        Places.initializeWithNewPlacesApiEnabled(
            applicationContext,
            getApiKey()
        )
    }

    private fun getApiKey(): String {

        val appInfo = packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        )

        return appInfo.metaData
            ?.getString("com.google.android.geo.API_KEY")
            .orEmpty()
    }

}