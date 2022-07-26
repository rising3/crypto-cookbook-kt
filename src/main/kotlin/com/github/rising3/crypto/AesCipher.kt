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

import java.nio.ByteBuffer
import java.security.AlgorithmParameters
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class AesCipher {
    private val algoAesCbc = "AES/CBC/PKCS5Padding"
    private val algoAesGcm = "AES/GCM/NoPadding"
    private val algoPbeAes = "PBEWithHmacSHA256AndAES_128"

    fun encrypt(src: ByteArray, key: SecretKey, iv: ByteArray): ByteArray {
        val cipher: Cipher = Cipher.getInstance(algoAesCbc)
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        return CryptoUtils.serialize(iv) + cipher.doFinal(src)
    }

    fun decrypt(src: ByteArray, key: SecretKey): ByteArray {
        val iv = CryptoUtils.deserialize(src)
        val cipher: Cipher = Cipher.getInstance(algoAesCbc)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv.first))
        return cipher.doFinal(iv.second)
    }

    fun gcmEncrypt(src: ByteArray, key: SecretKey, iv: ByteArray, add: ByteArray? = null): ByteArray {
        val cipher: Cipher = Cipher.getInstance(algoAesGcm)
        val parameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec)
        if (add != null) {
            cipher.updateAAD(add)
        }
        return CryptoUtils.serialize(iv) + cipher.doFinal(src)
    }

    fun gcmDecrypt(src: ByteArray, key: SecretKey, add: ByteArray? = null): ByteArray {
        val cipher: Cipher = Cipher.getInstance(algoAesGcm)
        val iv = CryptoUtils.deserialize(src)
        val parameterSpec = GCMParameterSpec(128, iv.first)
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec)
        if (add != null) {
            cipher.updateAAD(add)
        }
        return cipher.doFinal(iv.second)
    }

    fun pbEncrypt(
        src: ByteArray,
        password: String,
        salt: ByteArray,
        hashCount: Int,
        algorithm: String = algoPbeAes
    ): ByteArray {
        val key = CryptoUtils.generatePbeKey(password, salt, hashCount, algorithm)
        val params = CryptoUtils.getAlgorithmParameters(key)
        val algoParams = AlgorithmParameters.getInstance(key.algorithm)
        algoParams.init(params)
        val cipher: Cipher = Cipher.getInstance(key.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, algoParams)
        return CryptoUtils.serialize(params) + cipher.doFinal(src)
    }

    fun pbDecrypt(src: ByteArray, password: String, algorithm: String = algoPbeAes): ByteArray {
        val key = CryptoUtils.generatePbeKey(password, ByteArray(8), 1, algorithm) // salt and hash count is dummy values
        val params = CryptoUtils.deserialize(src)
        val algoParams = AlgorithmParameters.getInstance(key.algorithm)
        algoParams.init(params.first)
        val cipher: Cipher = Cipher.getInstance(key.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, algoParams)
        return cipher.doFinal(params.second)
    }
}
