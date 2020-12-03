package com.example.easymanager.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easymanager.R
import com.example.easymanager.adapters.LabelColorListAdapter
import kotlinx.android.synthetic.main.dialog_list.view.*

abstract class LabelColorListDialog (
    context: Context,
    private var list: ArrayList<String>,
    private val title: String = "",
    private var mSelectedColor: String = "" ): Dialog(context){

    private var adapter: LabelColorListAdapter? = null

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
        view.rv_dialog_list.layoutManager = LinearLayoutManager(context)
        adapter = LabelColorListAdapter(context, list, mSelectedColor)
        view.rv_dialog_list.adapter = adapter

        adapter!!.onColorItemClickListener =
            object: LabelColorListAdapter.OnColorItemClickListener {
                override fun onClick(position: Int, color: String) {
                    dismiss()
                    onColorSelected(color)
                }

            }
    }

    protected abstract fun onColorSelected(color: String)

}