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
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class Pbkdf2DigestTest {
    private val digest = Pbkdf2Digest()
    private val password = "password"
    private val salt = CryptoUtils.hexToByteArray("70617373776f726470617373776f7264")
    private val originalHash = CryptoUtils.hexToByteArray("000186a0000001000000001070617373776f726470617373776f7264232bcb7cf7cd99463bfa3688a2c33569bb372ecece343ada89e9861afb18d036")

    @Test
    fun testHash() {
        assertContentEquals(originalHash, digest.hash(password, salt))
    }

    @Test
    fun testVerify() {
        assertTrue(digest.verify(originalHash, password))
    }
}
