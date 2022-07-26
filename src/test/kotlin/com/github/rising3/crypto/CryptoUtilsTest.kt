/**
 * MIT License
 *
 * Copyright (c) 2022 Michio Nakagawa <michio.nakagawa@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.rising3.crypto

import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CryptoUtilsTest {
    private val text = "Hello, world!"
    private val textBase64 = "SGVsbG8sIHdvcmxkIQ=="

    @Test
    fun testGenerateBytes() {
        val actual = CryptoUtils.generateBytes(16)

        assertEquals(16, actual.size)
    }

    @Test
    fun testGenerateIV() {
        val actual = CryptoUtils.generateIV()

        assertEquals(16, actual.iv.size)
    }

    @Test
    fun testGenerateKey() {
        val algorithm = "AES"
        val actual = CryptoUtils.generateKey(256, algorithm)

        assertEquals(algorithm, actual.algorithm)
        assertEquals("RAW", actual.format)
        assertEquals(32, actual.encoded.size)
    }

    @Test
    fun testGeneratePbeKey() {
        val algorithm = "PBEWithHmacSHA512AndAES_256"
        val salt = CryptoUtils.generateBytes()
        val actual = CryptoUtils.generatePbeKey("password", salt, 10, algorithm)

        assertEquals(algorithm, actual.algorithm)
        assertEquals("RAW", actual.format)
        assertEquals(8, actual.encoded.size)
    }

    @Test
    fun testGeneratePbeKeyWithSamePasswordAndSalt() {
        val algorithm = "PBEWithHmacSHA512AndAES_256"
        val salt = CryptoUtils.hexToByteArray("606d33852106bf40")
        val expected = CryptoUtils.hexToByteArray("70617373776f7264") // encoded(same password, same salt)
        val actual = CryptoUtils.generatePbeKey("password", salt, 10, algorithm)

        assertEquals(algorithm, actual.algorithm)
        assertEquals("RAW", actual.format)
        assertContentEquals(expected, actual.encoded)
    }

    @Test
    fun testGenerateHmacKey() {
        val algorithm = "HmacSHA512"
        val actual = CryptoUtils.generateHmacKey(algorithm)

        assertEquals(algorithm, actual.algorithm)
        assertEquals("RAW", actual.format)
        assertEquals(64, actual.encoded.size)
    }

    @Test
    fun testRestoreKey() {
        val algorithm = "AES"
        val expected = CryptoUtils.hexToByteArray("d5b235e2b430287cae03ab9187524ed43b56dee3e9d392b48829cc28797acd3a")
        val actual = CryptoUtils.restoreKey(expected, algorithm)

        assertEquals(algorithm, actual.algorithm)
        assertEquals("RAW", actual.format)
        assertContentEquals(expected, actual.encoded)
    }

    @Test
    fun testGenerateKeyPair() {
        val algorithm = "DH"
        val actual = CryptoUtils.generateKeyPair(512, algorithm)

        assertEquals(algorithm, actual.public.algorithm)
        assertEquals("X.509", actual.public.format)
        assertEquals(algorithm, actual.private.algorithm)
        assertEquals("PKCS#8", actual.private.format)
    }

    @Test
    fun testRestorePublicKey() {
        val algorithm = "DH"
        val expected =
            CryptoUtils.hexToByteArray("3081df30819706092a864886f70d010301308189024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e170240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca40202018003430002404512c2aca7fc613cf8e919496f677ba5bf16e60863b235a05c43f599cf7e31c1be5e09c75ea8a6b47fd4810403dbc273045a6e8019b12ce1a156a72d31756509")
        val actual = CryptoUtils.restorePublicKey(expected, algorithm)

        assertEquals(algorithm, actual.algorithm)
        assertEquals("X.509", actual.format)
    }

    @Test
    fun testRestorePrivateKey() {
        val algorithm = "DH"
        val expected =
            CryptoUtils.hexToByteArray("3081d202010030819706092a864886f70d010301308189024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e170240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca40202018004330231009b8437541b60489cc67923c0f9464adad57b17d507284c682f9a56615f98834f2db236267e48b340c858dd3f1097f731")
        val actual = CryptoUtils.restorePrivateKey(expected, algorithm)

        assertEquals(algorithm, actual.algorithm)
        assertEquals("PKCS#8", actual.format)
    }

    @Test
    fun testToHexString() {
        val expected = byteArrayOf(0xA0.toByte(), 0xB0.toByte(), 0xE0.toByte(), 0xF0.toByte())
        val actual = CryptoUtils.toHexString(expected)

        assertEquals("a0b0e0f0", actual)
    }

    @Test
    fun testHexToByteArray() {
        val expected = byteArrayOf(0xA0.toByte(), 0xB0.toByte(), 0xE0.toByte(), 0xF0.toByte())

        assertContentEquals(expected, CryptoUtils.hexToByteArray("A0b0e0F0"))
        assertContentEquals(expected, CryptoUtils.hexToByteArray("A0 b0 e0 F0"))
    }

    @Test
    fun testBase64Enc() {
        assertEquals(textBase64, String(CryptoUtils.base64Enc(text)))
        assertEquals(textBase64, String(CryptoUtils.base64Enc(text.toByteArray())))
    }

    @Test
    fun testBase64Dec() {
        assertEquals(text, String(CryptoUtils.base64Dec(textBase64)))
        assertEquals(text, String(CryptoUtils.base64Dec(textBase64.toByteArray())))
    }

    @Test
    fun testToPem() {
        val regexPem =
            "-----BEGIN .*-----(\n|\r|\r\n)([0-9a-zA-Z\\+\\/=]{64}(\n|\r|\r\n))*([0-9a-zA-Z\\+\\/=]{1,63}(\n|\r|\r\n))?-----END .*-----(\n|\r|\r\n)?".toRegex()
        val actual = CryptoUtils.generateKeyPair(512)

        assertTrue(regexPem.matches(CryptoUtils.toPem(actual.public)))
        assertTrue(regexPem.matches(CryptoUtils.toPem(actual.private)))
    }

    @Test
    fun testPemToByteArray() {
        val key = CryptoUtils.generateKeyPair(512)
        val public = CryptoUtils.toPem(key.public)
        val private = CryptoUtils.toPem(key.private)

        assertContentEquals(key.public.encoded, CryptoUtils.pemToByteArray(public))
        assertContentEquals(key.private.encoded, CryptoUtils.pemToByteArray(private))
    }

    @Test
    fun testSerialize() {
        val expected = CryptoUtils.hexToByteArray("0000000548656c6c6f48656c6c6f")
        val actual = CryptoUtils.serialize("Hello".toByteArray()) + "Hello".toByteArray()

        assertContentEquals(expected, actual)
    }

    @Test
    fun testDeserialize() {
        val expected = "Hello".toByteArray()
        val actual = CryptoUtils.deserialize(CryptoUtils.hexToByteArray("0000000548656c6c6f") +  "Hello".toByteArray())

        assertContentEquals(expected, actual.first)
        assertContentEquals("Hello".toByteArray(), actual.second)
    }
}
