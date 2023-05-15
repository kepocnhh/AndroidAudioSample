package test.android.audio

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.TextView

internal class SpinnerAdapterGeneric<T : Any>(
    private val context: Context,
    private val values: List<T>,
) : BaseAdapter() {
    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): T {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val result: TextView = convertView as? TextView ?: TextView(context).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                128,
            )
        }
        result.text = getItem(position).toString()
        return result
    }
}

internal fun Spinner.setOnItemSelectedListener(block: (Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            block(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented: onNothingSelected")
        }
    }
}
