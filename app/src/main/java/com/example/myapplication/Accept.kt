package com.example.myapplication

class Accept {
    var senderId: String? = null
    var receiverId: String? = null
    var gameRef: String? = null

    constructor() {}

    constructor(senderId: String?, receiverId: String?, gameRef: String?) {
        this.senderId = senderId
        this.receiverId = receiverId
        this.gameRef = gameRef
    }
}