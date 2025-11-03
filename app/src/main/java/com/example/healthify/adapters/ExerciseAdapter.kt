package com.example.healthify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthify.R
import com.example.healthify.models.Exercise

class ExerciseAdapter(
    private val exerciseList: List<Exercise>
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvExerciseName)
        val target: TextView = view.findViewById(R.id.tvTargetMuscle)
        val equipment: TextView = view.findViewById(R.id.tvEquipment)
        val image: ImageView = view.findViewById(R.id.imgExercise)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.name.text = exercise.name
        holder.target.text = "Target: ${exercise.target}"
        holder.equipment.text = "Equipment: ${exercise.equipment}"

        Glide.with(holder.itemView.context)
            .load(exercise.gifUrl)
            .placeholder(R.drawable.bg_placeholder)
            .into(holder.image)
    }

    override fun getItemCount() = exerciseList.size
}
