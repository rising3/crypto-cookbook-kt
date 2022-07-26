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
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class AesCipherTest {
    private val text = "Hello, world!"
    private val cipher = AesCipher()
    private val key = SecretKeySpec(CryptoUtils.hexToByteArray("badce3d4684262b0b207effc475954dd"), "AES")
    private val iv = CryptoUtils.hexToByteArray("b3025e1e695289ceba86beecb4e8eec0")
    private val add = "/*** Authenticated Data ***/"
    private val password = "password"
    private val salt = CryptoUtils.hexToByteArray("70617373776f7264")

    @Test
    fun testEncrypt() {
        val encrypted = CryptoUtils.hexToByteArray("00000010b3025e1e695289ceba86beecb4e8eec0ce613f4ae10abd0e5111f7400be403dc")
        val actual = cipher.encrypt(text.toByteArray(), key, iv)

        assertContentEquals(encrypted, actual)
    }

    @Test
    fun testDecrypt() {
        val encrypted = CryptoUtils.hexToByteArray("00000010b3025e1e695289ceba86beecb4e8eec0ce613f4ae10abd0e5111f7400be403dc")
        val actual = cipher.decrypt(encrypted, key)

        assertContentEquals(text.toByteArray(), actual)
    }

    @Test
    fun testGcmEncrypt() {
        val encrypted = CryptoUtils.hexToByteArray("00000010b3025e1e695289ceba86beecb4e8eec0e1b090b1f4e90213cfc08f6d4fe711828fc6cfcee6ba35900b5d68d437")
        val actual = cipher.gcmEncrypt(text.toByteArray(), key, iv)

        assertContentEquals(encrypted, actual)
    }

    @Test
    fun testGcmDecrypt() {
        val encrypted = CryptoUtils.hexToByteArray("00000010b3025e1e695289ceba86beecb4e8eec0e1b090b1f4e90213cfc08f6d4fe711828fc6cfcee6ba35900b5d68d437")
        val actual = cipher.gcmDecrypt(encrypted, key)

        assertContentEquals(text.toByteArray(), actual)
    }

    @Test
    fun testGcmEncryptWithAdd() {
        val encrypted = CryptoUtils.hexToByteArray("00000010b3025e1e695289ceba86beecb4e8eec0e1b090b1f4e90213cfc08f6d4fe4bf52ff71b371ee32bf724ceb2dd3ef")
        val actual = cipher.gcmEncrypt(text.toByteArray(), key, iv, add.toByteArray())

        assertContentEquals(encrypted, actual)
    }

    @Test
    fun testGcmDecryptWithAdd() {
        val encrypted = CryptoUtils.hexToByteArray("00000010b3025e1e695289ceba86beecb4e8eec0e1b090b1f4e90213cfc08f6d4fe4bf52ff71b371ee32bf724ceb2dd3ef")
        val actual = cipher.gcmDecrypt(encrypted, key, add.toByteArray())

        assertContentEquals(text.toByteArray(), actual)
    }

    @Test
    fun testPbEncryption() {
        val actual = cipher.pbEncrypt(text.toByteArray(), password, salt, 10)
        println(CryptoUtils.toHexString(actual))
        assertContentEquals(text.toByteArray(), cipher.pbDecrypt(actual, password))
    }

    @Test
    fun testPbDecrypt() {
        val encrypted = CryptoUtils.hexToByteArray("0000005b3059303806092a864886f70d01050c302b041437df545d5bee8c19126930377cea1d14d5709bdd02021000020110300c06082a864886f70d02090500301d06096086480165030401020410934e1cf68072a67da3e4e899791fd09aebb868f6af7852f63b435c932ce8f66c")
        val actual = cipher.pbDecrypt(encrypted, password)

        assertContentEquals(text.toByteArray(), actual)
    }
}
