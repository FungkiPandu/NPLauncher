package com.example.nplauncher

import android.app.WallpaperManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.example.nplauncher.helper.PermissionHelper
import com.example.nplauncher.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.view.*


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        val textClock = view.home_textclock
//        textClock.setTextColor(
//            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
//                Configuration.UI_MODE_NIGHT_YES -> Color.WHITE
//                else -> Color.BLACK
//            }
//        )
//        textClock.setOnClickListener {
//            textClock.setTextColor(
//                if (textClock.currentTextColor == Color.BLACK) Color.WHITE else Color.BLACK
//            )
//        }
        if (PermissionHelper.isAllowReadStorage(this.requireContext())) {
            val wallpaperManager = WallpaperManager.getInstance(this.context)
            val wallpaperDrawable = wallpaperManager.drawable
            Palette.from(wallpaperDrawable.toBitmap()).generate {
                textClock.setTextColor(it?.vibrantSwatch?.titleTextColor ?: Color.WHITE)
            }
        }
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
