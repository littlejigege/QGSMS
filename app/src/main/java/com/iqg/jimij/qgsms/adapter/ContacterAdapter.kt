package com.iqg.jimij.qgsms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.iqg.jimij.qgsms.App
import com.iqg.jimij.qgsms.Const
import com.iqg.jimij.qgsms.R
import com.iqg.jimij.qgsms.model.beans.Contacter
import com.mobile.utils.gone
import com.mobile.utils.showToast
import com.mobile.utils.visiable
import kotlinx.android.synthetic.main.item_contacter.view.*
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/11/29.
 */
class ContacterAdapter(ctx: Context, val data: MutableList<Contacter> = mutableListOf()) : ArrayAdapter<Contacter>(ctx, R.layout.item_contacter, data) {
    private var lastPos = 0
    fun setData(data: MutableList<Contacter>) {
        clear()
        addAll(data)
        notifyDataSetChanged()
    }

    fun selectAll() {
        data.forEach {
            it.isSelected = true
        }
        notifyDataSetChanged()
    }

    fun getSelected() = data.filter { it.isSelected }.toMutableList()

    fun deleteSelected() {
        val list = getSelected()
        list.forEach {
            data.remove(it)
        }
        thread {
            list.forEach { App.db.contacterDao().deleteContacter(it) }
        }
        notifyDataSetChanged()
    }

    fun cancelSelected() {
        data.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    fun getPosByPinyin(py: Char): Int {
        (0..data.lastIndex).forEach {
            if (data[it].pinying == py) {
                lastPos = it
                println(data[it].pinying + "  " + py)
                println("lastpos == $lastPos")
                return it
            }
        }

        return lastPos
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val contacter = getItem(position)
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contacter, parent, false)
            view.tag = ViewHolder(view)
        } else {
            view = (convertView.tag as ViewHolder).view
        }
        with(contacter) {
            view.checkBox.isChecked = isSelected
            //设置checkbox
            view.checkBox.isClickable = false

            view.contacterName.text = name
            view.contacterPhone.text = phone
            println(sex)
            if (sex == Const.GIRL) {
                view.imageSex.visiable()
            } else {
                view.imageSex.gone()
            }
        }
        return view
    }

    inner class ViewHolder(var view: View)
}