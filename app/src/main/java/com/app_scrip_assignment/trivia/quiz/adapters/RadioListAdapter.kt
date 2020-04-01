package com.app_scrip_assignment.trivia.quiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.app_scrip_assignment.trivia.R
import com.app_scrip_assignment.trivia.quiz.models.Choice

class RadioListAdapter(
    val list: List<Choice>,
    private val observer: Observer<List<Choice>>
) :
    RecyclerView.Adapter<RadioListItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.radio_list_item, parent, false)
        return RadioListItemViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RadioListItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onItemClicked(position) }
        holder.radioButton.setOnClickListener { onItemClicked(position) }
        val choice = list[position]
        holder.radioButton.isChecked = choice.isSelected
        holder.valueText.text = choice.value
    }

    private fun onItemClicked(position: Int) {
        list.forEach { it.isSelected = false }
        list[position].isSelected = true
        notifyDataSetChanged()
        observer.onChanged(list)
    }

}

class RadioListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val radioButton: RadioButton = itemView.findViewById(R.id.choice_radio_button)
    val valueText: TextView = itemView.findViewById(R.id.tv_choice_value)
}