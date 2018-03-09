package com.iqg.jimij.qgsms.mainpage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.widget.NestedScrollView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.iqg.jimij.qgsms.App
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_contacter.view.*
import kotlin.concurrent.thread

class MainActivity : PermissionCompatActivity(), com.iqg.jimij.qgsms.mainpage.View {

    lateinit var sheet: BottomSheetBehavior<NestedScrollView>
    lateinit var adapter: ContacterAdapter
    val presenter = MainPresenter()
    val listViewBottom by lazy { listView.bottom }//记录原始listView底部坐标

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListView()//设置列表
        setupBottomSheet()//设置底部菜单
        setupButtons()
        presenter.takeView(this)
        presenter.getContacterFromDB()

    }

    private fun setupButtons() {
        btnPickAll.setOnClickListener { adapter.selectAll() }
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
        }
//        listView.setOnItemLongClickListener { _, _, position, _ ->
//            if (sheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
//                adapter.data[position].isSelected = true
//                adapter.showCheckBox()
//                sheet.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//            true
//        }
    }

    override fun showListView(contacters: MutableList<Contacter>) {
        adapter.setData(contacters)
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
                EditContacterDialog(this, presenter).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
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
