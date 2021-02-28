package com.example.nplauncher.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.nplauncher.R
import com.example.nplauncher.model.App
import kotlinx.android.synthetic.main.app_item.view.*

class AppListAdapter(private val list: MutableList<App>)
    : RecyclerView.Adapter<AppListAdapter.AppViewHolder>() {

    private var _onAppItemClick : ((App) -> Unit)? = null
    fun setOnAppItemClick(onAppItemClick: ((App) -> Unit)?) {
        _onAppItemClick = onAppItemClick
    }

    fun updateList(list: List<App>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AppViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app: App = list[position]
        holder.bind(app)
        holder.itemView.setOnClickListener {
            _onAppItemClick?.invoke(app)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class AppViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.app_item, parent, false)) {
        private var imgAppIcon: ImageView = itemView.app_icon
        private var txAppName: TextView = itemView.app_name

        fun bind(app: App) {
            imgAppIcon.setImageDrawable(app.image)
            txAppName.text = app.appLabel
            Palette.from(app.image.toBitmap()).generate {
                if (it != null) {
                    itemView.setBackgroundColor(it.getVibrantColor(Color.DKGRAY))
                }
            }
        }

    }
}