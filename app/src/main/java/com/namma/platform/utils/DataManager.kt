package com.namma.platform.utils

import android.content.Context
import com.namma.platform.models.Coach
import com.namma.platform.models.CoachType
import com.namma.platform.models.Station
import com.namma.platform.models.Train
import org.json.JSONObject

object DataManager {

    fun getStations(context: Context): List<Station> {
        val json = loadJson(context, "stations.json")
        val stationsArray = json.getJSONArray("stations")
        val stations = mutableListOf<Station>()
        for (i in 0 until stationsArray.length()) {
            val obj = stationsArray.getJSONObject(i)
            stations.add(
                Station(
                    code = obj.getString("code"),
                    name = obj.getString("name"),
                    nameKannada = obj.getString("nameKannada")
                )
            )
        }
        return stations
    }

    fun getTrainsForStation(context: Context, stationCode: String): List<Train> {
        val json = loadJson(context, "trains.json")
        val allTrains = json.getJSONArray("trains")
        val trains = mutableListOf<Train>()
        for (i in 0 until allTrains.length()) {
            val obj = allTrains.getJSONObject(i)
            if (obj.getString("stationCode") == stationCode) {
                trains.add(parseTrain(obj))
            }
        }
        return trains.sortedBy { it.arrivalTime }
    }

    private fun parseTrain(obj: JSONObject): Train {
        val coachArray = obj.getJSONArray("coachSequence")
        val coaches = mutableListOf<Coach>()
        for (j in 0 until coachArray.length()) {
            val coachObj = coachArray.getJSONObject(j)
            coaches.add(
                Coach(
                    type = CoachType.valueOf(coachObj.getString("type")),
                    label = coachObj.getString("label"),
                    position = coachObj.getInt("position")
                )
            )
        }
        return Train(
            trainNumber = obj.getString("trainNumber"),
            trainName = obj.getString("trainName"),
            trainNameKannada = obj.getString("trainNameKannada"),
            platform = obj.getInt("platform"),
            arrivalTime = obj.getString("arrivalTime"),
            destination = obj.getString("destination"),
            destinationKannada = obj.getString("destinationKannada"),
            coachSequence = coaches,
            status = obj.optString("status", "On Time")
        )
    }

    private fun loadJson(context: Context, fileName: String): JSONObject {
        val inputStream = context.assets.open(fileName)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return JSONObject(jsonString)
    }
}
