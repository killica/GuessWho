package com.example.myapplication

import com.google.firebase.database.IgnoreExtraProperties
import kotlin.random.Random

@IgnoreExtraProperties // Ignorise dodatna svojstva u klasi tokom serijalizacije
data class Game(
    var player1: String? = null,
    var player2: String? = null,
    var cardsDown1: Int = 0,
    var cardsDown2: Int = 0,
    var imageIndices: List<Int> = emptyList(),
    var player1image: Int = -1,
    var player2image: Int = -1,
    var finish: Int = 0
) {
    init {
        // Initialize imageIndices, player1image, and player2image in the constructor
        imageIndices = generateSequence { Random.nextInt(0, 41) }.distinct().take(24).toList().sorted()
        player1image = imageIndices[Random.nextInt(0, 24)]
        player2image = imageIndices[Random.nextInt(0, 24)]
    }

    override fun toString(): String {
        return player1 + " vs " + player2
    }
}