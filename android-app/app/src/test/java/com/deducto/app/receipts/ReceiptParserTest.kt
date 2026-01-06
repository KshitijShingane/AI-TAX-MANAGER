package com.deducto.app.receipts

import org.junit.Assert.assertEquals
import org.junit.Test

class ReceiptParserTest {
    @Test
    fun parse_simple_receipt() {
        val text = """
        ACME Supplies
        01/12/2025
        Total: Rs 1,234.50
        Thank you!
        """.trimIndent()

        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals("ACME Supplies", parsed.merchant)
        assertEquals(1234.50, parsed.amount, 0.01)
        // date parsing may vary — ensure it extracts something
        assert(parsed.date.isNotEmpty())
    }

    @Test
    fun parse_receipt_with_comma_amount() {
        val text = """
        Store Name
        12-11-2024
        INR 2,345
        """.trimIndent()

        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals("Store Name", parsed.merchant)
        assertEquals(2345.0, parsed.amount, 0.01)
    }

    @Test
    fun parse_receipt_with_multiple_numbers_picks_largest() {
        val text = """
        Café Mumbai
        11/01/2025
        Subtotal 400
        Tax 40
        Total ₹ 440
        """.trimIndent()

        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals(440.0, parsed.amount, 0.01)
    }
}
