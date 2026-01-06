package com.deducto.app.receipts

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class ReceiptParserEdgeCasesTest {
    private fun loadSample(filename: String): String {
        val path = Paths.get("android-app/testdata/receipts", filename)
        return String(Files.readAllBytes(path))
    }

    @Test
    fun parse_inr_symbol_and_code() {
        val text = loadSample("sample_inr_symbol.txt")
        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals("Store Name", parsed.merchant)
        assertEquals(2345.0, parsed.amount, 0.01)
    }

    @Test
    fun parse_total_label_and_pick_largest() {
        val text = loadSample("sample_total_label.txt")
        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals("Café Mumbai", parsed.merchant)
        assertEquals(440.0, parsed.amount, 0.01)
    }

    @Test
    fun skip_header_lines_and_extract_company_name() {
        val text = loadSample("sample_header_lines.txt")
        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals("Company Pvt Ltd", parsed.merchant)
        assertEquals(999.0, parsed.amount, 0.01)
    }

    @Test
    fun sample_simple_matches_existing_test() {
        val text = loadSample("sample_simple.txt")
        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals("ACME Supplies", parsed.merchant)
        assertEquals(1234.50, parsed.amount, 0.01)
        assertTrue(parsed.rawText.contains("Total"))
    }

    @Test
    fun parentheses_negative_amount_parsed_as_negative() {
        val text = """
        Refund
        (₹ 1,200.00)
        """.trimIndent()

        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals(-1200.0, parsed.amount, 0.01)
    }

    @Test
    fun amount_with_multiple_decimals_is_rounded() {
        val text = """
        Shop
        Total: Rs 123.4567
        """.trimIndent()

        val parsed = ReceiptParser.parseReceiptText(text)
        assertEquals(123.46, parsed.amount, 0.01)
    }
}

