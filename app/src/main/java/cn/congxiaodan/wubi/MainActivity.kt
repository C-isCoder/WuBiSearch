package cn.congxiaodan.wubi

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.BaseColumns
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private val adapter = WuBiAdapter()
    private lateinit var dialog: AlertDialog
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initView() {
        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.main)
        setSupportActionBar(toolbar)
        dialog = AlertDialog.Builder(this)
            .setTitle("🙃🙃🙃")
            .setMessage("正在初始化数据库，请稍后...")
            .setCancelable(false)
            .create()
        lvList.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        reset()
        return super.onOptionsItemSelected(item)
    }

    private fun initData() {
        sp = getSharedPreferences(TAG, Context.MODE_PRIVATE)
        val isOk = sp.getBoolean(TAG, false)
        if (!isOk) {
            initDb()
        }
    }

    private fun reset() {
        AlertDialog.Builder(this)
            .setTitle("提示‼️")
            .setMessage("确定要重新初始化数据库吗？")
            .setPositiveButton("确定") { _, _ ->
                initDb()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    fun search(view: View) {
        val text = etInput.text.toString()
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, R.string.tips_input_text, Toast.LENGTH_SHORT).show()
        } else {
            query(text)
        }
    }

    private fun query(text: String) {
        val db = WuBiDbHelper(this).readableDatabase
        val args = arrayOf(text)
        val cur = db.rawQuery(WuBiContract.SQL_QUERY, args)
        val list = mutableListOf<WuBiData>()
        while (cur.moveToNext()) {
            val indexId = cur.getColumnIndex(BaseColumns._ID)
            val id = cur.getInt(indexId)
            val indexHz = cur.getColumnIndex(WuBiContract.WuBiEntry.COLUMN_NAME_CHINESE)
            val chinese = cur.getString(indexHz)
            val indexZg = cur.getColumnIndex(WuBiContract.WuBiEntry.COLUMN_NAME_CODE)
            val code = cur.getString(indexZg)
            list.add(WuBiData(id, chinese, code))
        }
        cur.close()
        db.close()
        adapter.setList(list)
    }

    private fun initDb() {
        dialog.show()
        btnGo.isEnabled = false
        val helper = WuBiDbHelper(this)
        val db = helper.writableDatabase
        Thread(Runnable {
            val input = resources.openRawResource(R.raw.normal_text)
            input.bufferedReader(Charsets.UTF_16LE)
                .forEachLine {
                    val splits = it.split("\t")
                    Log.i(TAG, splits.toString())
                    val values = ContentValues()
                    values.put(WuBiContract.WuBiEntry.COLUMN_NAME_CHINESE, splits[0])
                    values.put(WuBiContract.WuBiEntry.COLUMN_NAME_CODE, splits[1])
                    val id = db.insert(WuBiContract.WuBiEntry.TABLE_NAME, null, values)
                    Log.i(TAG, "database insert result: $id}")
                }
            input.close()
            db.close()
            sp.edit().putBoolean(TAG, true).apply()
            runOnUiThread {
                dialog.dismiss()
                btnGo.isEnabled = true
            }
        }).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
    }
}
