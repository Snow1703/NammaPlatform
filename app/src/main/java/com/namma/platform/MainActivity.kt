package com.namma.platform

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.namma.platform.adapters.TrainAdapter
import com.namma.platform.databinding.ActivityMainBinding
import com.namma.platform.models.Station
import com.namma.platform.models.Train
import com.namma.platform.utils.DataManager
import com.namma.platform.utils.TTSManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ttsManager: TTSManager
    private lateinit var stations: List<Station>
    private lateinit var trainAdapter: TrainAdapter
    private var selectedStation: Station? = null
    private var trains: List<Train> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ttsManager = TTSManager(this)
        setupStationSpinner()
        setupRecyclerView()
        setupRefreshButton()
    }

    private fun setupStationSpinner() {
        stations = DataManager.getStations(this)
        val stationNames = stations.map { "${it.name} (${it.nameKannada})" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stationNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStation.adapter = adapter

        binding.spinnerStation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedStation = stations[position]
                loadTrains()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupRecyclerView() {
        trainAdapter = TrainAdapter(emptyList()) { train ->
            openTrainDetail(train)
        }
        binding.recyclerTrains.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = trainAdapter
        }
    }

    private fun setupRefreshButton() {
        binding.btnRefresh.setOnClickListener {
            loadTrains()
            ttsManager.speak("ಮಾಹಿತಿ ನವೀಕರಿಸಲಾಗಿದೆ. Information refreshed.")
        }
    }

    private fun loadTrains() {
        selectedStation?.let { station ->
            trains = DataManager.getTrainsForStation(this, station.code)
            trainAdapter.updateTrains(trains)

            if (trains.isEmpty()) {
                binding.tvNoTrains.visibility = View.VISIBLE
                binding.recyclerTrains.visibility = View.GONE
            } else {
                binding.tvNoTrains.visibility = View.GONE
                binding.recyclerTrains.visibility = View.VISIBLE
            }

            // Auto-announce first train
            trains.firstOrNull()?.let { firstTrain ->
                binding.tvNextTrain.text = "ಮುಂದಿನ ರೈಲು: Platform ${firstTrain.platform} | Next Train: Platform ${firstTrain.platform}"
            }
        }
    }

    private fun openTrainDetail(train: Train) {
        val intent = Intent(this, TrainDetailActivity::class.java).apply {
            putExtra("TRAIN_NUMBER", train.trainNumber)
            putExtra("STATION_CODE", selectedStation?.code)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}
