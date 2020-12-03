package com.example.easymanager.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easymanager.R
import kotlinx.android.synthetic.main.item_label_color.view.*

class LabelColorListAdapter(
    private val context: Context,
    private var list: ArrayList<String>,
    private val mSelectedColor: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onColorItemClickListener: OnColorItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_label_color, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val colorItem = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.view_main.setBackgroundColor(Color.parseColor(colorItem))
            if (colorItem == mSelectedColor) {
                holder.itemView.iv_selected_color.visibility = View.VISIBLE
            } else {
                holder.itemView.iv_selected_color.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                onColorItemClickListener?.let {
                    it.onClick(position,colorItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnColorItemClickListener {
        fun onClick(position: Int, color: String)
    }

}