package com.deducto.app.receipts

import java.util.regex.Pattern

object ReceiptParser {
    // Accepts symbols (₹), Rs, Rs., INR and optional prefixes like Total/Amount
    private val amountPattern = Pattern.compile("(?i)(?:total[:\s-]*)?(?:₹|Rs\.?|INR)?\s*([0-9]{1,3}(?:,[0-9]{3})*(?:\\.[0-9]{1,2})?)")
    private val datePatterns = listOf(
        Pattern.compile("(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4})"),
        Pattern.compile("(\\d{4}[/-]\\d{1,2}[/-]\\d{1,2})"),
        Pattern.compile("(\\d{1,2}\\s+[A-Za-z]{3,9}\\s+\\d{4})")
    )

    private val headerSkipPatterns = listOf(
        Pattern.compile("(?i)^(tax|invoice|receipt|bill|gst|sale|amount|total)")
    )

    fun parseReceiptText(text: String): ParsedReceipt {
        val lines = text.lines().map { it.trim() }.filter { it.isNotEmpty() }

        // Merchant heuristics: pick the first line that is not a common header
        val merchant = lines.firstOrNull { line ->
            headerSkipPatterns.none { p -> p.matcher(line).find() }
        } ?: (if (lines.isNotEmpty()) lines.first() else "")

        // Find amounts and pick the largest value (likely total)
        val amounts = mutableListOf<Double>()
        val matcher = amountPattern.matcher(text)
        while (matcher.find()) {
            val group = matcher.group(1) ?: continue
            val normalized = group.replace(",", "").toDoubleOrNull()
            normalized?.let { amounts.add(it) }
        }
        val amount = if (amounts.isNotEmpty()) amounts.maxOrNull()!! else 0.0

        // Find date
        var date: String? = null
        for (p in datePatterns) {
            val m = p.matcher(text)
            if (m.find()) {
                date = m.group(1)
                break
            }
        }

        return ParsedReceipt(
            merchant = merchant,
            date = date ?: "",
            amount = amount,
            currency = "INR",
            rawText = text
        )
    }
}

data class ParsedReceipt(
    val merchant: String,
    val date: String,
    val amount: Double,
    val currency: String,
    val rawText: String
)
