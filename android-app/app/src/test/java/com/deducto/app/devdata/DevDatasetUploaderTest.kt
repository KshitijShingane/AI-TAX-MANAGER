package com.deducto.app.devdata

import org.junit.Assert.assertEquals
import org.junit.Test

class DevDatasetUploaderTest {
    @Test
    fun merchant_hash_is_consistent() {
        val h1 = DevDatasetUploader.hashMerchant("ACME Supplies")
        val h2 = DevDatasetUploader.hashMerchant("ACME Supplies")
        assertEquals(h1, h2)
        // length for SHA-256 hex should be 64
        assertEquals(64, h1.length)
    }

    @Test
    fun bucket_make_sanity() {
        assertEquals("<100", DevDatasetUploader.makeBucket(50.0))
        assertEquals("100-499", DevDatasetUploader.makeBucket(200.0))
        assertEquals("500-999", DevDatasetUploader.makeBucket(700.0))
        assertEquals("1000-4999", DevDatasetUploader.makeBucket(1500.0))
        assertEquals("5000+", DevDatasetUploader.makeBucket(10000.0))
    }
}
