package com.namma.platform

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.namma.platform.adapters.CoachAdapter
import com.namma.platform.databinding.ActivityTrainDetailBinding
import com.namma.platform.models.Train
import com.namma.platform.utils.DataManager
import com.namma.platform.utils.TTSManager

class TrainDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainDetailBinding
    private lateinit var ttsManager: TTSManager
    private var currentTrain: Train? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ttsManager = TTSManager(this)

        val trainNumber = intent.getStringExtra("TRAIN_NUMBER") ?: return
        val stationCode = intent.getStringExtra("STATION_CODE") ?: return

        val trains = DataManager.getTrainsForStation(this, stationCode)
        currentTrain = trains.find { it.trainNumber == trainNumber }

        currentTrain?.let { train ->
            displayTrainDetails(train)
            setupCoachLayout(train)
            setupHelpButton(train)
            setupBackButton()
        }
    }

    private fun displayTrainDetails(train: Train) {
        binding.tvTrainName.text = train.trainName
        binding.tvTrainNameKannada.text = train.trainNameKannada
        binding.tvTrainNumber.text = "# ${train.trainNumber}"
        binding.tvPlatformNumber.text = "${train.platform}"
        binding.tvArrivalTime.text = train.arrivalTime
        binding.tvDestination.text = train.destination
        binding.tvDestinationKannada.text = train.destinationKannada
        binding.tvStatus.text = train.status

        val statusColor = if (train.status == "On Time")
            getColor(R.color.status_ontime) else getColor(R.color.status_delayed)
        binding.tvStatus.setTextColor(statusColor)
    }

    private fun setupCoachLayout(train: Train) {
        val coachAdapter = CoachAdapter(train.coachSequence)
        binding.recyclerCoachLayout.apply {
            layoutManager = LinearLayoutManager(
                this@TrainDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = coachAdapter
        }

        // Highlight general coach info
        val generalCoaches = train.coachSequence.filter {
            it.type == com.namma.platform.models.CoachType.GENERAL
        }
        if (generalCoaches.isNotEmpty()) {
            val positions = generalCoaches.joinToString(", ") { "Position ${it.position}" }
            binding.tvGeneralCoachInfo.text = "General Coach: $positions\nಸಾಮಾನ್ಯ ಬೋಗಿ: $positions"
            binding.tvGeneralCoachInfo.visibility = View.VISIBLE
        }
    }

    private fun setupHelpButton(train: Train) {
        binding.btnHelpMe.setOnClickListener {
            ttsManager.speakAnnouncement(
                trainName = train.trainName,
                trainNameKannada = train.trainNameKannada,
                platform = train.platform,
                destination = train.destination,
                destinationKannada = train.destinationKannada
            )
            binding.btnHelpMe.text = "🔊 ಮಾತನಾಡುತ್ತಿದೆ... Speaking..."
            binding.btnHelpMe.postDelayed({
                binding.btnHelpMe.text = "🔊 ಸಹಾಯ ಮಾಡಿ! Help Me!"
            }, 5000)
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}
