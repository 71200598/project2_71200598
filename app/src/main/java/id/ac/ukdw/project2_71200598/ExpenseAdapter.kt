package id.ac.ukdw.project2_71200598

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ExpenseAdapter(private val expenses: List<Expense>) : BaseAdapter() {

    override fun getCount(): Int {
        return expenses.size
    }

    override fun getItem(position: Int): Expense {
        return expenses[position]
    }

    override fun getItemId(position: Int): Long {
        return expenses[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val expense = getItem(position)
        viewHolder.tvKeterangan.text = expense.keterangan
        viewHolder.tvNominal.text = "Rp ${expense.nominal}"

        return view
    }

    private class ViewHolder(view: View) {
        val tvKeterangan: TextView = view.findViewById(android.R.id.text1)
        val tvNominal: TextView = view.findViewById(android.R.id.text2)
    }
}
