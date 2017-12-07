package com.graphicus.graphicus.utility

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import com.graphicus.graphicus.R

class SplashScreenHelper : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) { }

    override fun onActivityResumed(activity: Activity?) { }

    override fun onActivityStarted(activity: Activity?) { }

    override fun onActivityDestroyed(activity: Activity?) { }

    override fun onActivitySaveInstanceState(activity: Activity?, savedInstanceState: Bundle?) { }

    override fun onActivityStopped(activity: Activity?) { }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        try {
            val activityInfo = activity?.packageManager?.getActivityInfo(
                    activity.componentName, PackageManager.GET_META_DATA)
            val metaData = activityInfo?.metaData
            val theme = metaData?.getInt("theme", R.style.AppTheme) ?: R.style.AppTheme

            activity?.setTheme(theme)
        } catch (e: PackageManager.NameNotFoundException) {}
    }
}