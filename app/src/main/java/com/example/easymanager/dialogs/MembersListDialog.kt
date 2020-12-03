package com.example.easymanager.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easymanager.R
import com.example.easymanager.adapters.MembersListItemsAdapter
import com.example.easymanager.models.FireUser
import kotlinx.android.synthetic.main.dialog_list.view.*
import java.text.FieldPosition

abstract class MembersListDialog (
    context: Context,
    private var list: ArrayList<FireUser>,
    private val title: String = "" ): Dialog(context) {

    private var adapter: MembersListItemsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)
    }

    private fun setUpRecyclerView(view: View) {
        view.tv_dialog_title.text = title

        if (list.size > 0) {
            view.rv_dialog_list.layoutManager = LinearLayoutManager(context)
            adapter = MembersListItemsAdapter(context, list)
            view.rv_dialog_list.adapter = adapter

            adapter!!.setOnClickListener(object : MembersListItemsAdapter.OnMemberClickListener {
                    override fun onClick(position: Int, user: FireUser, action: String) {
                        dismiss()
                        onMemberSelected(user, action)
                    }
                })
        }
    }

    protected abstract fun onMemberSelected(user: FireUser, action: String)
}