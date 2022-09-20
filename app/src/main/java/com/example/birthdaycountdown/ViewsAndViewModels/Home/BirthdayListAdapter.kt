package com.example.birthdaycountdown.ViewsAndViewModels.Home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.birthdaycountdown.R
import com.example.birthdaycountdown.Shared.Models.CurrentStatesModel
import java.util.*

class BirthdayListAdapter(var birthdays: List<HomeViewModel.ListItem>) : RecyclerView.Adapter<BirthdayListAdapter.ViewHolder>() {
    private val TAG = BirthdayListAdapter::class.java.simpleName

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val birthday = birthdays[position]

        holder.name.text = birthday.name
        // format date as mm/dd/yyyy
        val calendar = Calendar.getInstance()
        calendar.time = birthday.date
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)
        holder.date.text = String.format("%02d/%02d/%d", month, day, year)

        // show starImg if birthday is today
        val today = Calendar.getInstance()
        if (calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH) &&
            calendar.get(Calendar.YEAR) <= today.get(Calendar.YEAR)) {
            holder.star.visibility = View.VISIBLE
        } else {
            holder.star.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            // set current birthday to the one clicked
            CurrentStatesModel.selectedBirthday = CurrentStatesModel.birthdays[position]
            holder.itemView.findNavController().navigate(R.id.action_nav_home_to_nav_gallery)
        }
    }

    override fun getItemCount(): Int {
        return birthdays.size
    }

    // create a class for the view holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val date: TextView
        val star: ImageView

        init {
            this.name = itemView.findViewById(R.id.nameTxt)
            this.date = itemView.findViewById(R.id.birthDateTxt)
            this.star = itemView.findViewById(R.id.starImg)
        }
    }
}