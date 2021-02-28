package com.example.nplauncher.viewmodel

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nplauncher.model.App

class AppListViewModel : ViewModel() {

    private var apps = mutableListOf<App>()
    private var _appsLiveData: MutableLiveData<List<App>> = MutableLiveData()

    val appsLiveData: LiveData<List<App>>
        get() = _appsLiveData


    fun loadApps(packageManager: PackageManager, appPackageName: String) {
        try {
            if (apps.isEmpty()) {
                val i = Intent(Intent.ACTION_MAIN, null)
                i.addCategory(Intent.CATEGORY_LAUNCHER)
                val availableApps =
                    packageManager.queryIntentActivities(i, 0)
                for (ri in availableApps) {
                    if (ri.activityInfo.packageName == appPackageName) continue
                    val appinfo = App(
                        ri.activityInfo.packageName,
                        ri.loadLabel(packageManager).toString(),
                        ri.activityInfo.loadIcon(packageManager)
                    )
                    apps.add(appinfo)
                }
                _appsLiveData.postValue(apps.sortedWith(compareBy { it.appLabel.capitalize() }))
            }
        } catch (ex: Exception) {
            Log.e("Error load Apps", ex.message.toString() + " loadApps")
        }
    }
}
