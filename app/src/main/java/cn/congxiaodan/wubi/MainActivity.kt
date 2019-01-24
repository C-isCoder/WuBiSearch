package cn.congxiaodan.wubi

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private val adapter = WuBiAdapter()
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initView() {
        dialog = AlertDialog.Builder(this)
            .setTitle("🙃")
            .setMessage("正在初始化数据库，请稍后...")
            .setCancelable(false)
            .create()
        lvList.adapter = adapter
    }

    private fun initData() {
        val sp = getSharedPreferences(TAG, Context.MODE_PRIVATE)
        val isOk = sp.getBoolean(TAG, false)
        if (!isOk) {
            dialog.show()
            btnGo.isEnabled = false
            val helper = WuBiDbHelper(this)
            val db = helper.writableDatabase
            Thread(Runnable {
                val input = resources.openRawResource(R.raw.wubi)
                input.bufferedReader(Charset.defaultCharset())
                    .forEachLine {
                        Log.d("CID", it.replace(";", ""))
                        db.execSQL(it.replace(";", ""))
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
            val indexId = cur.getColumnIndex(WuBiContract.WuBiEntry.COLUMN_NAME_ID)
            val id = cur.getString(indexId)
            val indexHz = cur.getColumnIndex(WuBiContract.WuBiEntry.COLUMN_NAME_HZ)
            val hanZi = cur.getString(indexHz)
            val indexZg = cur.getColumnIndex(WuBiContract.WuBiEntry.COLUMN_NAME_ZG)
            val ziGen = cur.getString(indexZg)
            list.add(WuBiData(id, hanZi, ziGen))
        }
        cur.close()
        adapter.setList(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
    }
}
