package com.deducto.app.receipts

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReceiptRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    private val collection = db.collection("receipts")

    suspend fun addReceipt(receipt: Receipt): String {
        val docRef = collection.add(receipt).await()
        return docRef.id
    }
}
