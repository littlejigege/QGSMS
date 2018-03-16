package com.iqg.jimij.qgsms.mainpage

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.widget.NestedScrollView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.iqg.jimij.qgsms.App
import com.iqg.jimij.qgsms.BuildConfig
import com.iqg.jimij.qgsms.Const
import com.iqg.jimij.qgsms.R
import com.iqg.jimij.qgsms.fragment.ServerDialog
import com.iqg.jimij.qgsms.adapter.ContacterAdapter
import com.iqg.jimij.qgsms.fragment.EditContacterDialog
import com.iqg.jimij.qgsms.fragment.SendsmsDialog
import com.iqg.jimij.qgsms.model.beans.Contacter
import com.mobile.utils.ActivityManager
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity
import com.mobile.utils.showToast
import com.vondear.rxtools.view.sidebar.WaveSideBarView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_contacter.view.*
import net.sourceforge.pinyin4j.PinyinHelper
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

class MainActivity : PermissionCompatActivity(), WaveSideBarView.OnTouchLetterChangeListener {


    lateinit var sheet: BottomSheetBehavior<NestedScrollView>
    lateinit var adapter: ContacterAdapter
    val mModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    val listViewBottom by lazy { listView.bottom }//记录原始listView底部坐标

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListView()//设置列表
        setupBottomSheet()//设置底部菜单
        setupButtons()
        //观察数据库数据
        mModel.contacterFromDB.observe(this, Observer {
            it?.let {
                updateTitle(it.size, adapter.getSelected().size)
                it.sort()
                adapter.setData(it)
            }
        })
        //获取动态接口
        thread {
            try {
                Const.serverAdress = URL(BuildConfig.API_GET_ADDRESS).readText()
                println(Const.serverAdress)
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("获取服务器地址失败，请确保网络畅通")
            }
        }

        indexBar.setOnTouchLetterChangeListener(this)
    }

    private fun setupButtons() {
        btnPickAll.setOnClickListener {
            adapter.selectAll()
            updateTitle(adapter.data.size, adapter.getSelected().size)
        }
        btnDelete.setOnClickListener {
            //删除并关闭菜单
            adapter.deleteSelected()
            sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        btnSend.setOnClickListener {
            Permission.SMS.doAfterGet(this) {
                val list = adapter.getSelected()
                if (list.isEmpty()) {
                    showToast("请选择联系人")
                } else {
                    SendsmsDialog(list).show(supportFragmentManager, "1")
                }
            }

        }
    }

    private fun setupBottomSheet() {
        sheet = BottomSheetBehavior.from(bottomSheet)
        sheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                listView.layout(listView.left, listView.top, listView.right
                        , ((listViewBottom - slideOffset * bottomSheet.height).toInt()))
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
    }

    private fun setupListView() {
        adapter = ContacterAdapter(this)
        listView.adapter = adapter
        //设置点击改变选择状态
        listView.setOnItemClickListener { _, view, position, _ ->
            //显示底部菜单
            if (sheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
                sheet.state = BottomSheetBehavior.STATE_EXPANDED
            }
            view.checkBox.toggle()
            adapter.data[position].isSelected = view.checkBox.isChecked
            updateTitle(adapter.data.size, adapter.getSelected().size)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.import_from_server -> {
                ServerDialog().show(supportFragmentManager, "")

            }
            R.id.import_manually -> {
                EditContacterDialog(this, mModel).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTitle(total: Int, selected: Int) {
        title = "QGSMS 人数:$total 已选:$selected"
    }

    override fun onLetterChange(p0: String) {
        listView.smoothScrollToPositionFromTop(adapter.getPosByPinyin(p0[0].toLowerCase()),0)
    }

    override fun onBackPressed() {
        //关闭菜单,并取消选择
        if (sheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            adapter.cancelSelected()
            sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            ActivityManager.doubleExit { }
        }
    }
}
