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
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class CryptoUtils {
    companion object {
        private val random = SecureRandom()

        @JvmStatic
        fun generateBytes(size: Int = 8): ByteArray {
            val bytes = ByteArray(size)
            random.nextBytes(bytes)
            return bytes
        }

        @JvmStatic
        fun generateIV(size: Int = 16): IvParameterSpec {
            val iv = generateBytes(size)
            return IvParameterSpec(iv)
        }

        @JvmStatic
        fun generateKey(size: Int = 128, algorithm: String = "AES"): SecretKey {
            val keyGen: KeyGenerator = KeyGenerator.getInstance(algorithm)
            if (size > 0) {
                keyGen.init(size)
            }
            return keyGen.generateKey()
        }

        @JvmStatic
        fun generatePbeKey(
            password: String, salt: ByteArray, hashCount: Int, algorithm: String = "PBEWithHmacSHA256AndAES_128"
        ): SecretKey {
            assert("pbewith.*".toRegex().matches(algorithm.lowercase()))

            val keySpec = PBEKeySpec(password.toCharArray(), salt, hashCount)
            val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance(algorithm)
            val key: SecretKey = keyFactory.generateSecret(keySpec)
            keySpec.clearPassword()
            return key
        }

        @JvmStatic
        fun generateHmacKey(algorithm: String = "HmacSHA256"): SecretKey {
            assert("hmac.*".toRegex().matches(algorithm.lowercase()))

            return generateKey(0, algorithm)
        }

        @JvmStatic
        fun restoreKey(src: ByteArray, algorithm: String = "AES"): SecretKey = SecretKeySpec(src, algorithm)

        @JvmStatic
        fun generateKeyPair(size: Int = 2048, algorithm: String = "RSA"): KeyPair {
            val keyGen = KeyPairGenerator.getInstance(algorithm)
            keyGen.initialize(size)
            return keyGen.generateKeyPair()
        }

        @JvmStatic
        fun getAlgorithmParameters(key: SecretKey): ByteArray {
            val cipher: Cipher = Cipher.getInstance(key.algorithm)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return cipher.parameters.encoded
        }

        @JvmStatic
        fun restorePublicKey(key: ByteArray, algorithm: String = "RSA"): PublicKey {
            val keyFactory: KeyFactory = KeyFactory.getInstance(algorithm)
            return keyFactory.generatePublic(X509EncodedKeySpec(key))
        }

        @JvmStatic
        fun restorePrivateKey(key: ByteArray, algorithm: String = "RSA"): PrivateKey {
            val keyFactory: KeyFactory = KeyFactory.getInstance(algorithm)
            return keyFactory.generatePrivate(PKCS8EncodedKeySpec(key))
        }

        @JvmStatic
        fun toHexString(bytes: ByteArray): String = HexFormat.of().formatHex(bytes)

        @JvmStatic
        fun hexToByteArray(hexString: String): ByteArray {
            assert("^([a-fA-F0-9]{2} ?)+$".toRegex().matches(hexString))

            return hexString.replace(" ", "").chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        }

        @JvmStatic
        fun toBase64(bytes: ByteArray): String = Base64.getEncoder().encodeToString(bytes)

        @JvmStatic
        fun base64Enc(src: String): ByteArray = base64Enc(src.toByteArray())

        @JvmStatic
        fun base64Enc(src: ByteArray): ByteArray = Base64.getEncoder().encode(src)

        @JvmStatic
        fun base64Dec(src: String): ByteArray = base64Dec(src.toByteArray())

        @JvmStatic
        fun base64Dec(src: ByteArray): ByteArray = Base64.getDecoder().decode(src)

        @JvmStatic
        fun toPem(key: PublicKey): String {
            val lineSeparator = System.getProperty("line.separator")
            val begin = listOf("-----BEGIN PUBLIC KEY-----")
            val end = listOf("-----END PUBLIC KEY-----")
            return listOf(begin, String(base64Enc(key.encoded))
                .chunked(64), end)
                .flatten()
                .joinToString(lineSeparator, "", lineSeparator)
        }

        @JvmStatic
        fun toPem(key: PrivateKey): String {
            val lineSeparator = System.getProperty("line.separator")
            val begin = listOf("-----BEGIN PRIVATE KEY-----")
            val end = listOf("-----END PRIVATE KEY-----")
            return listOf(begin, toBase64(key.encoded)
                .chunked(64), end)
                .flatten()
                .joinToString(lineSeparator, "", lineSeparator)
        }

        @JvmStatic
        fun pemToByteArray(key: String): ByteArray {
            val regexPem =
                "-----BEGIN .*-----(\n|\r|\r\n)([0-9a-zA-Z\\+\\/=]{64}(\n|\r|\r\n))*([0-9a-zA-Z\\+\\/=]{1,63}(\n|\r|\r\n))?-----END .*-----(\n|\r|\r\n)?".toRegex()
            assert(regexPem.matches(key))

            return base64Dec(key.replace("-----.*-----".toRegex(), "").replace("\n|\r|\r\n".toRegex(), ""))
        }

        fun serialize(data: ByteArray): ByteArray  = data.size.toByteArray() + data

        fun deserialize(data: ByteArray): Pair<ByteArray, ByteArray>  {
            val bb = ByteBuffer.wrap(data)
            val values = ByteArray(bb.int)
            bb.get(values)
            val others = ByteArray(bb.capacity() - bb.position())
            bb.get(others)
            return Pair(values, others)
        }

        fun intToByteArray(value: Int): ByteArray = value.toByteArray()

        private fun Int.toByteArray(): ByteArray = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this).array() // big endian
    }
}
