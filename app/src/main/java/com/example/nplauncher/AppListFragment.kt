package com.example.nplauncher

import android.Manifest
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nplauncher.adapter.AppListAdapter
import com.example.nplauncher.helper.PermissionHelper
import com.example.nplauncher.model.App
import com.example.nplauncher.viewmodel.AppListViewModel
import kotlinx.android.synthetic.main.app_list_fragment.*


class AppListFragment : Fragment() {
    companion object {
        fun newInstance() = AppListFragment()
    }

    private lateinit var viewModel: AppListViewModel
    private val appListAdapter = AppListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.app_list_fragment, container, false)

        if (PermissionHelper.isAllowReadStorage(this.requireContext())) {
            val wallpaperManager = WallpaperManager.getInstance(this.context)
            val wallpaperDrawable = wallpaperManager.drawable
            Palette.from(wallpaperDrawable.toBitmap()).generate {
                if (it != null) {
                    val bgColor = it.vibrantSwatch?.rgb ?: Color.BLACK
                    frag_app_list.setBackgroundColor(bgColor)
                    this.activity?.window?.statusBarColor = bgColor
                }
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AppListViewModel::class.java)

        initRecyclerView()

        loadAppList()
        viewModel.appsLiveData.observe(requireActivity(), Observer {
            updateRecyclerView(it)
        })
    }

    override fun onResume() {
        super.onResume()
        loadAppList()
    }

    private fun loadAppList() {
        viewModel.loadApps(requireActivity().packageManager)
    }

    private fun initRecyclerView() {
        rv_apps.layoutManager = GridLayoutManager(requireContext(), 5)
        rv_apps.adapter = appListAdapter
        appListAdapter.setOnAppItemClick {
            launchApp(it.appName)
        }
    }

    private fun updateRecyclerView(apps: List<App>) {
        appListAdapter.updateList(apps)
    }

    private fun launchApp(packageName: String) {
        val appIntent = requireActivity().packageManager.getLaunchIntentForPackage(packageName)
        appIntent?.let {
            startActivity(appIntent)
        }
    }
}
