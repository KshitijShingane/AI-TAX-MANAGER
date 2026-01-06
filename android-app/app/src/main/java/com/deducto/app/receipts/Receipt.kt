package com.deducto.app.receipts

import com.google.firebase.Timestamp

data class Receipt(
    val id: String = "",
    val merchant: String = "",
    val date: String = "",
    val amount: Double = 0.0,
    val currency: String = "INR",
    val rawText: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
