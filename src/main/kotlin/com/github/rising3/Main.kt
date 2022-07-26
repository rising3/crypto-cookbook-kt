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
package com.github.rising3

import com.github.rising3.crypto.*

const val TEXT = "Hello, world!"

fun main() {
    digestExample()
    println()

    pbkdf2Example()
    println()

    cbcCipherExample()
    println()

    gcmCipherExample()
    println()

    passwordBasedEncryptionExample()
    println()

    hmacExample()
    println()

    rsaCipherExample()
    println()

    rsaSignExample()
    println()

    dhExample()
}

private fun digestExample() {
    val algorithms = listOf("MD5", "SHA-1", "SHA-256", "SHA-512")
    val digest = Digest()
    println("=== hash example ===")
    println("plain text: $TEXT")
    algorithms.forEach { algorithm ->
        val hash = digest.hash(TEXT.toByteArray(), algorithm)
        val verify = digest.verify(TEXT.toByteArray(), hash, algorithm)
        println("  $algorithm:")
        println("    hash: ${CryptoUtils.toHexString(hash)}")
        println("    verify: $verify")
    }
}

private fun pbkdf2Example() {
    val digest = Pbkdf2Digest("PBKDF2WithHmacSHA512")
    val password = "password"
    val salt = CryptoUtils.generateBytes(16)
    val hash = digest.hash(password, salt)
    val verify = digest.verify(hash, password)
    println("=== PBKDF2 example ===")
    println("password: $password")
    println("salt: ${CryptoUtils.toHexString(salt)}")
    println("hash: ${CryptoUtils.toHexString(hash)}")
    println("verify: $verify")
}

private fun cbcCipherExample() {
    val cipher = AesCipher()
    val key = CryptoUtils.generateKey()
    val iv = CryptoUtils.generateBytes(16)
    val decodeKey = CryptoUtils.restoreKey(key.encoded)
    val enc = cipher.encrypt(TEXT.toByteArray(), key, iv)
    val dec = cipher.decrypt(enc, decodeKey)
    println("=== aes cipher(CBC) example ===")
    println("plain text: $TEXT")
    println("  key: ${CryptoUtils.toHexString(key.encoded)}")
    println("  iv: ${CryptoUtils.toHexString(iv)}")
    println("  encrypted: ${CryptoUtils.toHexString(enc)}")
    println("  decrypted: ${String(dec)}")
}

private fun gcmCipherExample() {
    val cipher = AesCipher()
    val key = CryptoUtils.generateKey()
    val iv = CryptoUtils.generateBytes(16)
    val add = "Authenticated Data".toByteArray()
    val decodeKey = CryptoUtils.restoreKey(key.encoded)
    val enc = cipher.gcmEncrypt(TEXT.toByteArray(), key, iv, add)
    val dec = cipher.gcmDecrypt(enc, decodeKey, add)
    println("=== aes cipher(GCM) example ===")
    println("plain text: $TEXT")
    println("  key: ${CryptoUtils.toHexString(key.encoded)}")
    println("  iv: ${CryptoUtils.toHexString(iv)}")
    println("  encrypted: ${CryptoUtils.toHexString(enc)}")
    println("  decrypted: ${String(dec)}")
}

private fun passwordBasedEncryptionExample() {
    val cipher = AesCipher()
    val algorithm = "PBEWithHmacSHA512AndAES_256"
    val password = "password"
    val salt = CryptoUtils.generateBytes(16)
    val enc = cipher.pbEncrypt(TEXT.toByteArray(), password, salt, 10, algorithm)
    val dec = cipher.pbDecrypt(enc, password, algorithm)

    println("=== password based encryption example ===")
    println("plain text: $TEXT")
    println("  password: $password")
    println("  salt: ${CryptoUtils.toHexString(salt)}")
    println("  encrypted: ${CryptoUtils.toHexString(enc)}")
    println("  decrypted: ${String(dec)}")
}

private fun hmacExample() {
    val algorithms = listOf("HmacMD5", "HmacSHA1", "HmacSHA256", "HmacSHA512")
    val hmac = Hmac()

    println("=== hmac example ===")
    println("plain text: $TEXT")
    algorithms.forEach { algorithm ->
        val key = CryptoUtils.generateHmacKey(algorithm)
        val mac = hmac.mac(TEXT.toByteArray(), key)
        val verify = hmac.verify(mac, TEXT.toByteArray(), key)
        println("  $algorithm:")
        println("    key: ${CryptoUtils.toHexString(key.encoded)}")
        println("    mac: ${CryptoUtils.toHexString(mac)}")
        println("    verify: $verify")
    }
}

private fun rsaCipherExample() {
    val cipher = RsaCipher()
    val key = CryptoUtils.generateKeyPair(512)
    val enc = cipher.encrypt(TEXT.toByteArray(), key.public)
    val dec = cipher.decrypt(enc, key.private)
    println("=== rsa cipher example ===")
    println("PEM - public : \n${CryptoUtils.toPem(key.public)}")
    println("PEM - private: \n${CryptoUtils.toPem(key.private)}")
    println("plain text: $TEXT")
    println("public encrypt -> private decrypt:")
    println("  encrypted: ${CryptoUtils.toHexString(enc)}")
    println("  decrypted: ${String(dec)}")
}

private fun rsaSignExample() {
    val key = CryptoUtils.generateKeyPair(512)
    val sign = RsaSign()
    val signed = sign.sign(TEXT.toByteArray(), key.private)
    val verify = sign.verify(signed, TEXT.toByteArray(), key.public)
    println("=== rsa sign/verify example ===")
    println("PEM - public : \n${CryptoUtils.toPem(key.public)}")
    println("PEM - private: \n${CryptoUtils.toPem(key.private)}")
    println("plain text: $TEXT")
    println("  signed: ${CryptoUtils.toHexString(signed)}")
    println("  verify: $verify")
}

private fun dhExample() {
    val dh = DiffieHellman()
    val alice = dh.createDiffieHellman(512)
    val bob = dh.getDiffieHellman(alice.prime, alice.generator)

    // If DH public key
    // val aliceSecret = df.computeSecret(bob.DhPublicKey, alice.DhPrivateKey)
    // val bobSecret = df.computeSecret(alice.DhPublicKey, bob.DhPrivateKey)

    // If DH public y value
    val aliceSecret = dh.computeSecret(alice.prime, alice.generator, bob.y, alice.DhPrivateKey)
    val bobSecret = dh.computeSecret(alice.prime, alice.generator, alice.y, bob.DhPrivateKey)

    println("=== Diffie Hellman example ===")
    println("alice...")
    println("  key: ${CryptoUtils.toHexString(alice.DhPublicKey.encoded)}")
    println("  prime: ${alice.prime}")
    println("  generator: ${alice.generator}")
    println("bob...")
    println("  key: ${CryptoUtils.toHexString(bob.DhPublicKey.encoded)}")
    println("exchange and generate the secrets...")
    println("  alice secret: " + CryptoUtils.toHexString(aliceSecret.encoded))
    println("  bob   secret: " + CryptoUtils.toHexString(bobSecret.encoded))
    println("  compare secrets: ${aliceSecret == bobSecret}")
}
