package com.example.healthify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthify.R
import com.example.healthify.models.Meal

class MealAdapter(
    private val meals: MutableList<Meal>,
    var onDeleteClick: ((Meal) -> Unit)? = null // optional callback
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMealName: TextView = itemView.findViewById(R.id.tvMealName)
        val tvCalories: TextView = itemView.findViewById(R.id.tvCalories)
        val tvMacros: TextView = itemView.findViewById(R.id.tvMacros)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.tvMealName.text = meal.name
        holder.tvCalories.text = "${meal.calories} kcal"
        holder.tvMacros.text = "P: %.1fg | C: %.1fg | F: %.1fg".format(meal.protein, meal.carbs, meal.fat)


        holder.btnDelete.setOnClickListener {
            onDeleteClick?.invoke(meal)
        }
    }

    override fun getItemCount(): Int = meals.size
}
