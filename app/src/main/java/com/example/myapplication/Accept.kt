package com.example.myapplication

class Accept {
    var senderId: String? = null
    var receiverId: String? = null

    constructor() {}

    constructor(senderId: String?, receiverId: String?) {
        this.senderId = senderId
        this.receiverId = receiverId
    }
}