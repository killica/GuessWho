package com.example.myapplication

import kotlin.random.Random

class Game {
    private var player1: String? = null
    private var player2: String? = null
    private var cardsDown1 : Int = 0
    private var cardsDown2 : Int = 0
    private var imageIndices = IntArray(24)
    private var player1image : Int = -1
    private var player2image : Int = -1
    private var finish : Int = 0



    constructor() {}

    constructor(player1: String?, player2: String?) {
        this.player1 = player1
        this.player2 = player2
        imageIndices = IntArray(24) { Random.nextInt(0, 41) }.distinct().take(24).toIntArray().sortedArray()
        player1image = imageIndices[Random.nextInt(0, 25)]
        player2image = imageIndices[Random.nextInt(0, 25)]
    }

}