package com.example.healthify.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthify.R
import com.example.healthify.models.Exercise

class ExerciseAdapter(
    private val exerciseList: List<Exercise>,
    private val onItemClick: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtExerciseName)
        val target: TextView = view.findViewById(R.id.txtExerciseType)
        val equipment: TextView = view.findViewById(R.id.txtExerciseEquipment)
        val image: ImageView = view.findViewById(R.id.imgExercise)
        val btnComplete: Button = view.findViewById(R.id.btnComplete)
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
        Log.d("ExerciseAdapter", "gifUrl = ${exercise.gifUrl}")


        Glide.with(holder.itemView.context)
            .asGif()
            .load(exercise.gifUrl)
            .centerCrop()
            .placeholder(R.drawable.bg_placeholder)
            .error(R.drawable.bg_placeholder_error)
            .into(holder.image)

        holder.itemView.setOnClickListener { onItemClick(exercise) }
    }

    override fun getItemCount() = exerciseList.size
}
