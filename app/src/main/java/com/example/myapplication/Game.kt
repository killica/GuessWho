package com.example.myapplication

import com.google.firebase.database.IgnoreExtraProperties
import kotlin.random.Random

@IgnoreExtraProperties
data class Game(
    var player1: String? = null,
    var player2: String? = null,
    var cardsDown1: Long = 0,
    var cardsDown2: Long = 0,
    var imageIndices: List<Long> = emptyList(),
    var player1image: Long = -1,
    var player2image: Long = -1,
    var finish: Long = 0
) {
    init {
        // Initialize imageIndices, player1image, and player2image in the constructor
        imageIndices = generateSequence { Random.nextLong(0, 40) }.distinct().take(24).toList().sorted()
        player1image = imageIndices[Random.nextInt(0, 24)]
        player2image = imageIndices[Random.nextInt(0, 24)]
    }

    override fun toString(): String {
        return player1 + " vs " + player2
    }

}