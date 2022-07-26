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

class RsaSignTest {
    private val text = "Hello, world!"
    private val sign = RsaSign()
    private val publicKey =
        CryptoUtils.restorePublicKey(CryptoUtils.hexToByteArray("305c300d06092a864886f70d0101010500034b003048024100aa0d4be11c61ba049e624090b2e4c2ef523032508847daed7f0dbb10bb0eadae204e227016a1fc65f7578fb3396d04a1d6a83cbb7d181ac2f96a160c11a2f9150203010001"))
    private val privateKey =
        CryptoUtils.restorePrivateKey(CryptoUtils.hexToByteArray("30820153020100300d06092a864886f70d01010105000482013d30820139020100024100aa0d4be11c61ba049e624090b2e4c2ef523032508847daed7f0dbb10bb0eadae204e227016a1fc65f7578fb3396d04a1d6a83cbb7d181ac2f96a160c11a2f915020301000102402696cdd94faf7d9efeb21d24b8f3e0a89e660184f4e8196e3b9eca0c89e652d30a1f7af8b444f4cda974a981e3cb92fce5019de0fdf65c38463916fb92ec6b21022100d1499860d93b51fb612a7a3cf7b25d2c1f996161b24368f7f0248270dce81e53022100d001d5cbc69952ed2a08f40612202a8cd9a602d402e3aacb21c2a9f8f3de8df7022018f829d8ec31fa9efe41be21c5ff9ec423e4fdcc55235bc3b0fffa1c130f128702204006e0956a1b55f054c90ebc33a61d12e007fec4dde2d076d87c802f7679a1ff0220452d27776cbf457996378611fce22e755d71f1cffa0ef2eb882e20d67428dc3f"))

    @Test
    fun testRsaSign() {
        val signed =
            CryptoUtils.hexToByteArray("6d77726c3e672b0405c055f825ae591f699624241e60caa6f2141898be454087f96e51d3f3ed61fed0ede390de4fc48f6b18b5ba530deadc30fdb8e9fc41f445")
        val actual = sign.sign(text.toByteArray(), privateKey)

        assertContentEquals(signed, actual)
    }

    @Test
    fun testRsaSignVerify() {
        val signed =
            CryptoUtils.hexToByteArray("6d77726c3e672b0405c055f825ae591f699624241e60caa6f2141898be454087f96e51d3f3ed61fed0ede390de4fc48f6b18b5ba530deadc30fdb8e9fc41f445")

        assertTrue(sign.verify(signed, text.toByteArray(), publicKey))
    }
}
