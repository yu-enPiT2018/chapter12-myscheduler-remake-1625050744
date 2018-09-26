package com.example.enpit_p12.myscheduler

import android.support.v7.widget.DecorContentParent
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.text.format.DateFormat.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import kotlinx.android.synthetic.*
import java.text.DateFormat
import java.text.FieldPosition

class ScheduleAdapter(data: OrderedRealmCollection<Shcedule>?) : RealmBaseAdapter<Shcedule>(data) {

    inner class ViewHolder(cell: View) {
        val date = cell.findViewById<TextView>(android.R.id.text1)
        val title = cell.findViewById<TextView>(android.R.id.text2)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        when (convertView) {
            null -> {
                val inflater = LayoutInflater.from(parent?.context)
                view = inflater.inflate(android.R.layout.simple_list_item_2, parent,
                        false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }

        }

        adapterData?.run {
            val schdule = get(position)
            viewHolder.date.text =
                    format("yyyy/MM/dd", schdule.date)
            viewHolder.title.text = schdule.title
        }
        return view
    }

}