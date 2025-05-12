package com.example.ricewise

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class VarietyHomeAdapter(private val items: List<HomeCategory>) :
    RecyclerView.Adapter<VarietyHomeAdapter.VarietyViewHolder>() {

    inner class VarietyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textVarietyName)
        val card: CardView = view.findViewById(R.id.cardContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VarietyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_variety_card, parent, false)
        return VarietyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VarietyViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.name

        val bgColor = when (item.category.lowercase()) {
            "recommended" -> Color.parseColor("#C8E6C9") // light green
            "top" -> Color.parseColor("#BBDEFB")         // light blue
            "my" -> Color.parseColor("#FFE0B2")          // light orange
            else -> Color.LTGRAY
        }

        holder.card.setCardBackgroundColor(bgColor)
    }

    override fun getItemCount() = items.size
}
