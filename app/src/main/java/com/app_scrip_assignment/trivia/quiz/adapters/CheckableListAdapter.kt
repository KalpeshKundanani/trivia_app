package com.app_scrip_assignment.trivia.quiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.app_scrip_assignment.trivia.R
import com.app_scrip_assignment.trivia.quiz.models.Choice

class CheckableListAdapter(
    val list: List<Choice>,
    private val observer: Observer<List<Choice>>
) :
    RecyclerView.Adapter<CheckableListItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkable_list_item, parent, false)
        return CheckableListItemViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CheckableListItemViewHolder, position: Int) {
        holder.valueText.text = list[position].value
        holder.checkBox.isChecked = list[position].isSelected
        holder.itemView.setOnClickListener { onItemClicked(position) }
        holder.checkBox.setOnClickListener { onItemClicked(position) }
    }

    private fun onItemClicked(position: Int) {
        list[position].isSelected = !list[position].isSelected
        observer.onChanged(list)
        notifyDataSetChanged()
    }
}

class CheckableListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val checkBox: CheckBox = itemView.findViewById(R.id.choice_check_box)
    val valueText: TextView = itemView.findViewById(R.id.tv_choice_value)
}