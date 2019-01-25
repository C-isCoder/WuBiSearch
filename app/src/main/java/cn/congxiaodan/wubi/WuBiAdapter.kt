package cn.congxiaodan.wubi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class WuBiAdapter : BaseAdapter() {
    private val list = mutableListOf<WuBiData>()

    fun setList(list: List<WuBiData>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val data = list[position]
        val holder: Holder
        val view: View
        if (convertView == null) {
            holder = Holder()
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_wubi, parent, false)
            holder.tvId = view.findViewById(R.id.tvId)
            holder.tvChinese = view.findViewById(R.id.tvChinese)
            holder.tvCode = view.findViewById(R.id.tvCode)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as Holder
        }
        holder.tvId?.text = data.id.toString()
        holder.tvChinese?.text = data.chinese
        holder.tvCode?.text = data.code
        return view
    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    class Holder {
        var tvId: TextView? = null
        var tvChinese: TextView? = null
        var tvCode: TextView? = null
    }
}