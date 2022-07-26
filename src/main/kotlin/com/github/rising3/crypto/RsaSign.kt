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

import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

class RsaSign(algoSignature: String = "SHA256withRSA") {
    private val algoSignature = algoSignature

    init {
        assert("sha.*withrsa".toRegex().matches(algoSignature.lowercase()))
    }

    fun sign(src: ByteArray, privateKey: PrivateKey): ByteArray {
        val sign: Signature = Signature.getInstance(algoSignature)
        sign.initSign(privateKey)
        sign.update(src)
        return sign.sign()
    }

    fun verify(sign: ByteArray, src: ByteArray, publicKey: PublicKey): Boolean {
        val verify = Signature.getInstance(algoSignature)
        verify.initVerify(publicKey)
        verify.update(src)
        return verify.verify(sign)
    }
}
