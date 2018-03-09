package com.iqg.jimij.qgsms.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.AdapterView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iqg.jimij.qgsms.App
import com.iqg.jimij.qgsms.R
import com.iqg.jimij.qgsms.adapter.ContacterAdapter
import com.iqg.jimij.qgsms.mainpage.MainActivity
import com.iqg.jimij.qgsms.model.beans.Contacter
import com.mobile.utils.*
import kotlinx.android.synthetic.main.dialog_server.view.*
import kotlinx.android.synthetic.main.item_contacter.view.*
import kotlinx.coroutines.experimental.delay
import java.net.URL
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/11/30.
 */
class ServerDialog : DialogFragment() {
    val MOBILE = 5
    val BACKGROUND = 2
    val FRONT = 1
    val DESIGN = 7
    val FLUSHBONAD = 3
    val GAME = 4
    val DATA = 6
    lateinit var rootView: View
    val adapter: ContacterAdapter by lazy { ContacterAdapter(context) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)//无标题
        rootView = inflater.inflate(R.layout.dialog_server, container, false)
        rootView.listViewServer.adapter = adapter
        //设置点击改变选择状态
        rootView.listViewServer.setOnItemClickListener { _, view, position, _ ->
            view.checkBox.toggle()
            adapter.data[position].isSelected = view.checkBox.isChecked
        }
        //设置确定按钮
        rootView.btnImport.setOnClickListener {
            thread {
                adapter.getSelected().forEach {
                    App.db.contacterDao().insertContacter(it)
                }
                (activity as MainActivity).presenter.getContacterFromDB()
                inUiThread { dismiss() }
            }
        }
        //设置全选
        rootView.btnPickAll2.setOnClickListener {
            adapter.selectAll()
        }
        setupSpinner()
        return rootView
    }

    private fun setupSpinner() {
        rootView.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getContacters(position + 1)

            }
        }
    }

    private fun getContacters(group: Int) {

        rootView.progressBar.visiable()
        coroutine({
            var text: String = ""
            try {
                text = URL("${App.serverAdress}$group").readText()
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("与服务器连接失败，请确认连接校园网")
            }
            println(text)
            text
        }) {
            var list: MutableList<Contacter> = mutableListOf()
            if (it != "[]") {
                try {
                    list = Gson().fromJson(it, object : TypeToken<MutableList<Contacter>>() {}.type)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else{
                showToast("暂无数据")
            }
            rootView.progressBar.gone()
            adapter.setData(list)
        }
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
        super.onResume()
    }
}