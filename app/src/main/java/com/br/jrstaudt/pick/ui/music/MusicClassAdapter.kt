package com.br.jrstaudt.pick.ui.music

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.jrstaudt.pick.R
import com.br.jrstaudt.pick.models.MusicClass
import kotlinx.android.synthetic.main.fragment_music_class_list_item.view.*
import java.text.ParseException
import java.text.SimpleDateFormat

class MusicClassAdapter(private val context: Context,
                        private val classItems: List<MusicClass>): RecyclerView.Adapter<MusicClassAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var listClass: TextView = view.findViewById(R.id.tvItem)
        var listDateClass: TextView = view.findViewById(R.id.tvDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_music_class_list_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = classItems[position]
        holder.listClass.setText(listItem.description)
        holder.listDateClass.text = formatDate(listItem.date!!)
    }

    private fun formatDate(date: String): String {
        try {
            val formatIni = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateRecieved = formatIni.parse(date)
            val formatFin = SimpleDateFormat("d MMM, yyyy")

            return formatFin.format(dateRecieved)
    } catch (e: ParseException) {
        return  ""
        }
    }

    override fun getItemCount(): Int {
        return classItems.size
    }
}