# crypto cookbook for Kotlin
[![Build](https://github.com/rising3/crypto-cookbook-kt/actions/workflows/build.yml/badge.svg)](https://github.com/rising3/crypto-cookbook-kt/actions/workflows/build.yml)

How to use `javax.crypto` for Java.

Prerequisites:
* Java 17 or higher
* Kotlin 1.7.10 or higher
* Gradle 7.5 or higher

## Examples for crypto

### Hash

Creating hash digests of data.

``` 
=== hash example ===
plain text: Hello, world!
  MD5:
    hash: 6cd3556deb0da54bca060b4c39479839
    verify: true
  SHA-1:
    hash: 943a702d06f34599aee1f8da8ef9f7296031d699
    verify: true
  SHA-256:
    hash: 315f5bdb76d078c43b8ac0064e4a0164612b1fce77c869345bfc94c75894edd3
    verify: true
  SHA-512:
    hash: c1527cd893c124773d811911970c8fe6e857d6df5dc9226bd8a160614c0cd963a4ddea2b94bb7d36021ef9d865d5cea294a82dd49a0bb269f51f6e7a57f79421
    verify: true
```
### PBKDF2

Creating hash digests of password.

```
=== PBKDF2 example ===
password: password
salt: 4abc18f8eac395252a76afb25a150ed2
hash: 000186a000000100000000104abc18f8eac395252a76afb25a150ed25b8e0e69c1259c5195ef362587b9e124069ef255922608706ec1ca4ccc5edb18
verify: true
```

### AES Cipher

Encrypt and decrypt data.

```
=== aes cipher(CBC) example ===
plain text: Hello, world!
  key: a57ee7f2af8c0d285670fcf7cdd67830
  iv: d4fa61ce44d2d0a79f4c06eef7c35983
  encrypted: 00000010d4fa61ce44d2d0a79f4c06eef7c35983db777cf171313a3635f3e219e584fca8
  decrypted: Hello, world!

=== aes cipher(GCM) example ===
plain text: Hello, world!
  key: 0affbb3affa84d1837b2ac48f5fda110
  iv: 85ddcaed30b4340e6a94271959b14907
  encrypted: 0000001085ddcaed30b4340e6a94271959b14907ccbdbc8471271599da0e585bc3dec081a8c50cdc7c815dd8c1c47a2587
  decrypted: Hello, world!
```

### Password based encryption

Encrypt and decrypt data, using password.

```
=== password based encryption example ===
plain text: Hello, world!
  password: password
  salt: 127e0a3d8dae76c6
  key: 70617373776f7264
  encrypted: 0000005b3059303806092a864886f70d01050c302b04142c783966342044501423d4757629b4e35ea16abf02021000020110300c06082a864886f70d02090500301d06096086480165030401020410abd5c615cedde276b4cb3b05ddb1c26b4826a7cc9ef31d3545f7c5a203645aac
  decrypted: Hello, world!
```

### HMAC

Creating cryptographic HMAC digests.

```
=== hmac example ===
plain text: Hello, world!
  HmacMD5:
    key: 060cd499a7afe0cbd247b2ae7b0e55f294b50d647f301b739bce3604c57f93c39b2a53e1187faeb6e1479f6d02b6b14edb8df8df64035b4c273ffdb0a2f10644
    mac: 19b3a8d314c58c496769afb0411ae544
    verify: true
  HmacSHA1:
    key: b97f10cee898d087fb61a9c5c71daba28e8f1732e0b2c2094e9778c20af2ad0d4b895f1138c958e2a6d8d67102119eb8643cfde82c08e672c7dfd97e08f89dea
    mac: 194df5847ce1887ce4e77c2d496a53c955920137
    verify: true
  HmacSHA256:
    key: 29cf02dcc464b7c00b29c01fc1b8efa1acf76e05d9ff0695bb52ddeb18f8d00f
    mac: a5c211db8cd482d40652e7412d04ac48724c166f7e25b4d4aef03327ceb735b7
    verify: true
  HmacSHA512:
    key: d81ab88f12ca1f35e3e2f9588d87382733d67404b3d605c9dd2214811846a8ceee4976afc4536722812a8233a804f43c35453afcbc7a9b9cb8e50000623d1356
    mac: 53b47e4776d36031aa0e81c23b4cff5dacd7428e8361a913ed0ae22eda04ca5a8281054e469d45c2aa2245af03e03159d31a8b452aad5ccf480f19eae7b3133e
    verify: true
``` 

### RSA Cipher

Encrypt and decrypt data, using RSA key pair.

``` 
=== rsa cipher example ===
PEM - public : 
-----BEGIN PUBLIC KEY-----
MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM6bm6dBPvVoXe+9QCY2uaiiuyd0YVuq
ervZ9NRiOkk2Sk6O2FTxFDguQSFheAYqsQ2coJRf5WbKdHCQDvsIWtMCAwEAAQ==
-----END PUBLIC KEY-----

PEM - private: 
-----BEGIN PRIVATE KEY-----
MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAzpubp0E+9Whd771A
Jja5qKK7J3RhW6p6u9n01GI6STZKTo7YVPEUOC5BIWF4BiqxDZyglF/lZsp0cJAO
+wha0wIDAQABAkAFR87boHxGc2trNDAOifmGyNn1sTDH0fyfDnwGbZhFxLSuEfAw
6zOjByFUlI7GWGZkq/drwxe47MEUaFKMy1QZAiEA8fe+u96YsQradyF6kBxSDvif
CbpR94mNy7K0olvyZL0CIQDalujUprvzkmMrtLlOeyqX1L6bU1Pamq43KO7+aLHe
zwIgECrooK737uR1wn4HLXfm978fCUxcy7hyNAklzq9sG9UCIQCC4gNNePxnLorU
26RtGpB5UvhCNtWpuh6AhrD7giKmvwIgG3Kp+eXOpbQpb1tFr3nQYtsg9uQYfKIQ
WkkbqU6ZM+o=
-----END PRIVATE KEY-----

plain text: Hello, world!
public encrypt -> private decrypt:
  encrypted: 41ca2fe7759cbf498226fdc1b30292ae6ff39eb2778676f5722fecddba966ea0cba5d95aa353531294ca126a296fe2d2033bacd284fa8c375ee7ac39c0cb3cbf
  decrypted: Hello, world!
``` 

### RSA Sign/Verify

Generating and verifying signatures.

``` 
=== rsa sign/verify example ===
PEM - public : 
-----BEGIN PUBLIC KEY-----
MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIr4zdm7xWaNsJiVc+pN7zhDjBHl6y8S
kQi4dcbkXJ5gnP7C3tNf/xLWaA+GMPB9PYaI1plFsJfDDv2Db0qwymECAwEAAQ==
-----END PUBLIC KEY-----

PEM - private: 
-----BEGIN PRIVATE KEY-----
MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAivjN2bvFZo2wmJVz
6k3vOEOMEeXrLxKRCLh1xuRcnmCc/sLe01//EtZoD4Yw8H09hojWmUWwl8MO/YNv
SrDKYQIDAQABAkADDJFLd2NmU6v+ti7VFQWdG42Hw/PV+/2J4JYCqNgBqerkWHfd
iDI0j7n9HyOov3ZlCv6nxO3cJ6nxlYDz/1ORAiEAkA0riCmyug8qvoyr1gGPJ5F4
hyMECH0dTa2BtA3rQi8CIQD2+RBdsw84OvnQzK54Ui02bC+QHUczDRUwSX4KhvRo
bwIgYbTNJhpCY/kNoRmBJDMCinAdNGwHvEpAqgNZymr2e3kCIQCkxoWJzmawIID+
MyGQe1tY/RM6tp4DTI4/ejkSsvmsFwIgELV7rWvA8Wi+mgfVrvFzoouLjWYT3eG4
bv19lCDR2j8=
-----END PRIVATE KEY-----

plain text: Hello, world!
  signed: 1be0049140e2c2b9fcaf51b191bb4c03ad06c1695141f7ef5a79ad50266e52d3ee4a203e9b7a35fa9b679f0ae5f328210220ae57aab883aaf75b7dd7b82c9d59
  verify: true
```

### Diffie Hellman

Creating Diffie-Hellman key exchanges.

```
=== Diffie Hellman example ===
alice...
  key: 3081df30819706092a864886f70d010301308189024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e170240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca40202018003430002405d72e75a26006c0c2731593ee70db5e235aa2896d8b9a25528add271cd412c37b4e9acb29e21413976f0859368c2d09debef92a409d39f62541e6cc9cbc0c8d9
  prime: 13232376895198612407547930718267435757728527029623408872245156039757713029036368719146452186041204237350521785240337048752071462798273003935646236777459223
  generator: 5421644057436475141609648488325705128047428394380474376834667300766108262613900542681289080713724597310673074119355136085795982097390670890367185141189796
bob...
  key: 3081e030819706092a864886f70d010301308189024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e170240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca402020180034400024100a655da150aef704adc40574f0ff95510ff7997fdce7636afaf6e962c7c29708e4b4eeb40bb163c5019084cebc36471ec776e18e7f4e40a37c53c816dcc0f40e4
exchange and generate the secrets...
  alice secret: 6ee3ce7d871c8efd6a965e7839998492eeaa5dba5b39a97c8a21ad99c858c830b6af98bc9559a52cdf44bb8cad08c005c234df189ee1f7bb2e91f74d34735057
  bob   secret: 6ee3ce7d871c8efd6a965e7839998492eeaa5dba5b39a97c8a21ad99c858c830b6af98bc9559a52cdf44bb8cad08c005c234df189ee1f7bb2e91f74d34735057
  compare secrets: true
```

## Installation

```bash
$ git clone https://github.com/rising3/crypto-cookbook-kt.git
$ cd crypto-cookbook-kt
$ ./gradlew init
```

## Usage

### build and test

Use the gradle to build crypto cookbook.

```bash
$ ./gradlew build
```

## License

This project is licensed under the MIT License - see the LICENSE file for details
