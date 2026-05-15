package com.namma.platform.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.namma.platform.R
import com.namma.platform.models.Train

class TrainAdapter(
    private var trains: List<Train>,
    private val onTrainClick: (Train) -> Unit
) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    inner class TrainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTrain: CardView = view.findViewById(R.id.card_train)
        val tvTrainName: TextView = view.findViewById(R.id.tv_train_name)
        val tvTrainNameKannada: TextView = view.findViewById(R.id.tv_train_name_kannada)
        val tvTrainNumber: TextView = view.findViewById(R.id.tv_train_number)
        val tvPlatform: TextView = view.findViewById(R.id.tv_platform)
        val tvArrivalTime: TextView = view.findViewById(R.id.tv_arrival_time)
        val tvDestination: TextView = view.findViewById(R.id.tv_destination)
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_train, parent, false)
        return TrainViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val train = trains[position]
        holder.tvTrainName.text = train.trainName
        holder.tvTrainNameKannada.text = train.trainNameKannada
        holder.tvTrainNumber.text = "#${train.trainNumber}"
        holder.tvPlatform.text = "Platform ${train.platform}"
        holder.tvArrivalTime.text = train.arrivalTime
        holder.tvDestination.text = "${train.destination}\n${train.destinationKannada}"
        holder.tvStatus.text = train.status

        val statusColor = if (train.status == "On Time")
            holder.itemView.context.getColor(R.color.status_ontime)
        else
            holder.itemView.context.getColor(R.color.status_delayed)
        holder.tvStatus.setTextColor(statusColor)

        // Highlight first train differently
        if (position == 0) {
            holder.cardTrain.setCardBackgroundColor(
                holder.itemView.context.getColor(R.color.next_train_highlight)
            )
        } else {
            holder.cardTrain.setCardBackgroundColor(
                holder.itemView.context.getColor(R.color.card_background)
            )
        }

        holder.cardTrain.setOnClickListener { onTrainClick(train) }
    }

    override fun getItemCount() = trains.size

    fun updateTrains(newTrains: List<Train>) {
        trains = newTrains
        notifyDataSetChanged()
    }
}
