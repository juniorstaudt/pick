package com.br.jrstaudt.pick

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric

class PickApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        configureCrashReporting()
    }

    private fun configureCrashReporting() {
        val crashlyticsCore = CrashlyticsCore.Builder()
            .disabled(BuildConfig.DEBUG)
            .build()
        Fabric.with(this,
            Crashlytics.Builder().core(crashlyticsCore).build())
    }
}