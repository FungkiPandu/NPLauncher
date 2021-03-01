package com.example.nplauncher.viewmodel

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nplauncher.model.App
import me.xdrop.fuzzywuzzy.FuzzySearch

class AppListViewModel : ViewModel() {

    private var apps = mutableListOf<App>()
    private var _appsLiveData: MutableLiveData<List<App>> = MutableLiveData()

    val appsLiveData: LiveData<List<App>>
        get() = _appsLiveData

    fun searchApps(query: String) {
        if (query.isBlank()) {
            _appsLiveData.postValue(apps)
            return
        }
        val search = FuzzySearch.extractTop(query, apps, { item -> item?.appLabel ?: "" }, 20, 70)
        _appsLiveData.postValue(search.map { it.referent })
    }

    fun loadApps(packageManager: PackageManager, appPackageName: String) {
        try {
            val temp = mutableListOf<App>()
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
                temp.add(appinfo)
            }
            apps.clear()
            apps.addAll(temp.sortedWith(compareBy { it.appLabel.capitalize() }))
            _appsLiveData.postValue(apps)
        } catch (ex: Exception) {
            Log.e("Error load Apps", ex.message.toString() + " loadApps")
        }
    }
}
