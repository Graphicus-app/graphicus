package com.graphicus.graphicus

import android.app.Application
import com.graphicus.graphicus.utility.SplashScreenHelper

class GraphicusApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // register the util to remove splash screen after loading
        registerActivityLifecycleCallbacks(SplashScreenHelper())
    }

}