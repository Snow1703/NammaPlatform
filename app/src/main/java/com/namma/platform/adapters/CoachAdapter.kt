package com.namma.platform.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.namma.platform.R
import com.namma.platform.models.Coach
import com.namma.platform.models.CoachType

class CoachAdapter(
    private val coaches: List<Coach>
) : RecyclerView.Adapter<CoachAdapter.CoachViewHolder>() {

    inner class CoachViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardCoach: CardView = view.findViewById(R.id.card_coach)
        val tvCoachLabel: TextView = view.findViewById(R.id.tv_coach_label)
        val tvCoachType: TextView = view.findViewById(R.id.tv_coach_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coach, parent, false)
        return CoachViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoachViewHolder, position: Int) {
        val coach = coaches[position]
        holder.tvCoachLabel.text = coach.label
        holder.tvCoachType.text = getKannadaLabel(coach.type)

        val bgColor = when (coach.type) {
            CoachType.ENGINE -> holder.itemView.context.getColor(R.color.coach_engine)
            CoachType.GENERAL -> holder.itemView.context.getColor(R.color.coach_general)
            CoachType.SLEEPER -> holder.itemView.context.getColor(R.color.coach_sleeper)
            CoachType.AC -> holder.itemView.context.getColor(R.color.coach_ac)
            CoachType.LADIES -> holder.itemView.context.getColor(R.color.coach_ladies)
            CoachType.GUARD -> holder.itemView.context.getColor(R.color.coach_guard)
        }
        holder.cardCoach.setCardBackgroundColor(bgColor)
    }

    override fun getItemCount() = coaches.size

    private fun getKannadaLabel(type: CoachType): String = when (type) {
        CoachType.ENGINE -> "ಎಂಜಿನ್\nEngine"
        CoachType.GENERAL -> "ಸಾಮಾನ್ಯ\nGeneral"
        CoachType.SLEEPER -> "ಸ್ಲೀಪರ್\nSleeper"
        CoachType.AC -> "ಎ.ಸಿ.\nA.C."
        CoachType.LADIES -> "ಮಹಿಳೆ\nLadies"
        CoachType.GUARD -> "ಗಾರ್ಡ್\nGuard"
    }
}
