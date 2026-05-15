package com.namma.platform.models

data class Train(
    val trainNumber: String,
    val trainName: String,
    val trainNameKannada: String,
    val platform: Int,
    val arrivalTime: String,
    val destination: String,
    val destinationKannada: String,
    val coachSequence: List<Coach>,
    val status: String = "On Time"
)

data class Coach(
    val type: CoachType,
    val label: String,
    val position: Int
)

enum class CoachType {
    ENGINE,
    GENERAL,
    SLEEPER,
    AC,
    LADIES,
    GUARD
}

data class Station(
    val code: String,
    val name: String,
    val nameKannada: String
)
