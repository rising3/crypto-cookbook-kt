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

import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.KeyAgreement
import javax.crypto.SecretKey
import javax.crypto.interfaces.DHPrivateKey
import javax.crypto.interfaces.DHPublicKey
import javax.crypto.spec.DHParameterSpec
import javax.crypto.spec.DHPublicKeySpec
import javax.crypto.spec.SecretKeySpec

data class DiffieHellmanParams(
    val key: KeyPair,
    val DhPrivateKey: DHPrivateKey,
    val DhPublicKey: DHPublicKey,
    val prime: BigInteger,
    val generator: BigInteger,
    val y: BigInteger,
)

class DiffieHellman {
    private val algoDh = "DH"

    fun createDiffieHellman(primeLength: Int = 2048): DiffieHellmanParams {
        val keyPair = CryptoUtils.generateKeyPair(primeLength, algoDh)
        val publicKey: DHPublicKey = keyPair.public as DHPublicKey
        val privateKey: DHPrivateKey = keyPair.private as DHPrivateKey
        return DiffieHellmanParams(keyPair, privateKey, publicKey, publicKey.params.p, publicKey.params.g, publicKey.y)
    }

    fun getDiffieHellman(prime: BigInteger, generator: BigInteger): DiffieHellmanParams {
        val paramSpec = DHParameterSpec(prime, generator)
        val keyGen = KeyPairGenerator.getInstance(algoDh)
        keyGen.initialize(paramSpec)
        val keyPair = keyGen.generateKeyPair()
        val publicKey: DHPublicKey = keyPair.public as DHPublicKey
        val privateKey: DHPrivateKey = keyPair.private as DHPrivateKey
        return DiffieHellmanParams(keyPair, privateKey, publicKey, publicKey.params.p, publicKey.params.g, publicKey.y)
    }

    fun computeSecret(prime: BigInteger, generator: BigInteger, othersY: BigInteger, key: DHPrivateKey): SecretKey {
        val publicKeySpec = DHPublicKeySpec(othersY, prime, generator)
        val keyFactory = KeyFactory.getInstance(algoDh)
        val othersPublicKey: DHPublicKey = keyFactory.generatePublic(publicKeySpec) as DHPublicKey
        return computeSecret(othersPublicKey, key)
    }

    fun computeSecret(othersPublicKey: DHPublicKey, key: DHPrivateKey): SecretKey {
        val keyAgreement = KeyAgreement.getInstance(algoDh)
        keyAgreement.init(key)
        keyAgreement.doPhase(othersPublicKey, true)
        return SecretKeySpec(keyAgreement.generateSecret(), "AES")
    }
}
