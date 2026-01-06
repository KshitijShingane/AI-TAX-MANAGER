package com.deducto.app.devdata

import com.deducto.app.receipts.ParsedReceipt
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DevDatasetUploader {
    private val db = FirebaseFirestore.getInstance()
    private const val COLLECTION = "dev_receipts"
    private const val PARSER_VERSION = "parser-v1"

    fun hashMerchant(merchant: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(merchant.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun extractYearMonth(dateStr: String): String? {
        // Try common formats
        val patterns = listOf("d/M/yyyy", "d-M-yyyy", "yyyy/M/d", "yyyy-M-d", "d MMM yyyy")
        for (p in patterns) {
            try {
                val dt = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(p))
                return dt.format(DateTimeFormatter.ofPattern("yyyy-MM"))
            } catch (_: DateTimeParseException) {
                // try next
            }
        }
        return null
    }

    data class UploadRecord(
        val merchantHash: String,
        val dateMonth: String?,
        val amountRounded: Double,
        val amountBucket: String,
        val currency: String,
        val parserVersion: String,
        val uid: String?,
        val createdAt: Timestamp = Timestamp.now()
    )

    fun makeBucket(amount: Double): String {
        val a = kotlin.math.abs(amount)
        return when {
            a < 100 -> "<100"
            a < 500 -> "100-499"
            a < 1000 -> "500-999"
            a < 5000 -> "1000-4999"
            else -> "5000+"
        }
    }

    fun uploadParsedReceipt(parsed: ParsedReceipt, receiptId: String? = null) {
        val merchantHash = hashMerchant(parsed.merchant.take(100))
        val dateMonth = if (parsed.date.isNotEmpty()) extractYearMonth(parsed.date) else null
        val amountRounded = kotlin.math.round(parsed.amount * 100.0) / 100.0
        val amountBucket = makeBucket(amountRounded)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val record = UploadRecord(
            merchantHash = merchantHash,
            dateMonth = dateMonth,
            amountRounded = amountRounded,
            amountBucket = amountBucket,
            currency = parsed.currency,
            parserVersion = PARSER_VERSION,
            uid = uid
        )

        val doc = if (receiptId != null) db.collection(COLLECTION).document(receiptId) else db.collection(COLLECTION).document()
        doc.set(record)
    }
}
