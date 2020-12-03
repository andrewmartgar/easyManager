package com.example.easymanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easymanager.R
import com.example.easymanager.models.SelectedMembers
import kotlinx.android.synthetic.main.item_card_selected_member.view.*

open class CardMembersListItemAdapter(
    private val context: Context,
    private val list: ArrayList<SelectedMembers>,
    private val assignedMembers: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

   private var onClickListener: OnMemberSmallItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_card_selected_member, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            if (position == list.size - 1 && assignedMembers) {
                holder.itemView.iv_add_member.visibility = View.VISIBLE
                holder.itemView.iv_selected_member_image.visibility = View.GONE
            } else {
                holder.itemView.iv_add_member.visibility = View.GONE
                holder.itemView.iv_selected_member_image.visibility = View.VISIBLE

                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_place_holder_grey)
                    .into(holder.itemView.iv_selected_member_image)
            }

            holder.itemView.setOnClickListener {
                onClickListener?.let{
                    it.onClick()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnMemberSmallItemClickListener {
        fun onClick()
    }

    fun setOnClickListener(onMemberSmallItemClickListener: OnMemberSmallItemClickListener) {
        this.onClickListener = onMemberSmallItemClickListener
    }

}