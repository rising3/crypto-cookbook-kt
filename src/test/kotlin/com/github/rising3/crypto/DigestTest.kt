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

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class DigestTest {
    private val digest = Digest()

    @ParameterizedTest
    @MethodSource("sourceAlgorithms")
    fun testDigest(algorithm: String, src: ByteArray, originalHash: ByteArray) {
        val actual = digest.hash(src, algorithm)

        assertContentEquals(originalHash, actual)
        assertTrue(digest.verify(originalHash, src, algorithm))
    }

    companion object {
        private val src = "Hello, world!".toByteArray()

        @JvmStatic
        fun sourceAlgorithms(): List<Arguments> = listOf(
            Arguments.of("MD5", src, CryptoUtils.hexToByteArray("6cd3556deb0da54bca060b4c39479839")),
            Arguments.of("SHA-1", src, CryptoUtils.hexToByteArray("943a702d06f34599aee1f8da8ef9f7296031d699")),
            Arguments.of(
                "SHA-256",
                src,
                CryptoUtils.hexToByteArray("315f5bdb76d078c43b8ac0064e4a0164612b1fce77c869345bfc94c75894edd3")
            ),
            Arguments.of(
                "SHA-512",
                src,
                CryptoUtils.hexToByteArray("c1527cd893c124773d811911970c8fe6e857d6df5dc9226bd8a160614c0cd963a4ddea2b94bb7d36021ef9d865d5cea294a82dd49a0bb269f51f6e7a57f79421")
            )
        )
    }
}
