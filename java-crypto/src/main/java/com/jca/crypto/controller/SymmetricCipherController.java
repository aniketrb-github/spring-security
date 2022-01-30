package com.jca.crypto.controller;

import com.jca.crypto.entity.SymmetricEncryption;
import com.jca.crypto.keygeneration.SymmetricKeyGeneration;
import com.jca.crypto.symmetric.AESCipherAPI;
import com.jca.crypto.symmetric.ChaChaCipherAPI;
import com.jca.crypto.util.CommonConstants;
import com.jca.crypto.util.SecureRandomAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Aniket Bharsakale
 */
@RestController
@RequestMapping(value = "/crypt")
public class SymmetricCipherController {

    @Autowired
    private SymmetricKeyGeneration symmetricKeyGeneration;

    @Autowired
    private SecureRandomAPI secureRandomAPI;

    @Autowired
    private AESCipherAPI aesCipherAPI;

    @Autowired
    private ChaChaCipherAPI chaChaCipherAPI;

    /**
     * This endpoint expects encryption algorithm and key size to generate a symmetric key.
     * Generated Symmetric Key should be safely stored. Ideally using a KMS.
     * Sample request : curl --location --request POST 'http://localhost:8080/crypt/generate-symmetric-key' --header 'Content-Type: application/json' --data-raw '{"key_size":"256", "enc_algo":"AES"}'
     *
     * @param symmetricEncryption : key_size (specified in bit size) and enc_algo properties should be passed in json request
     * @return : Symmetric key (base 64 encoded):
     * {
     * "key_size": 256,
     * "enc_algo": "AES",
     * "symmetric_key": "tTE23BRNMGiNbYUPB22R66Ha8gaaD0FsaPpTcw2fm2k="
     * }
     */
    @PostMapping(value = "/generate-symmetric-key",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    SymmetricEncryption generateSymmetricKey(@RequestBody SymmetricEncryption symmetricEncryption) {
        symmetricEncryption.setBase64EncodedKey(
                symmetricKeyGeneration.generateSymmetricKey(
                        symmetricEncryption.getEncAlgo(),
                        symmetricEncryption.getKeySize(),
                        secureRandomAPI.drbgSecureRandom()
                ));
        return symmetricEncryption;
    }

    /**
     * This endpoint generates initialization vector of specified size.
     * Generated IV should be treated as any keying material and be safely stored. Ideally using a KMS.
     * Sample request: curl --location --request POST 'http://localhost:8080/crypt/generate-initialization-vector' --header 'Content-Type: application/json' --data-raw '{"iv_size":"16"}'
     *
     * @param symmetricEncryption : iv_size (specified in byte size) property should be passed in json request
     * @return : Outputs, generated IV (base 64 encoded):
     * {
     * "iv_size": 16,
     * "IV": "nvsjNGLMK/1Hjn2aw7+n+A=="
     * }
     */
    @PostMapping(value = "/generate-initialization-vector",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    SymmetricEncryption generateInitVector(@RequestBody SymmetricEncryption symmetricEncryption) {
        byte[] iv = new byte[symmetricEncryption.getIvSize()];
        SecureRandom secureRandom = secureRandomAPI.drbgSecureRandom();
        secureRandom.nextBytes(iv);
        symmetricEncryption.setBase64EncodeIV(Base64.getEncoder().encodeToString(iv));
        return symmetricEncryption;
    }

    /***
     * /encrypt endpoint is called to do the actual encryption, taking in the key and iv generated thru above endpoints. It gives out the cipher text.
     * Sample request:
     * curl --location --request POST 'http://localhost:8080/crypt/encrypt' \
     * --header 'Content-Type: application/json' \
     * --data-raw '{
     *     "symmetric_key": "tTE23BRNMGiNbYUPB22R66Ha8gaaD0FsaPpTcw2fm2k=",
     *     "IV": "nvsjNGLMK/1Hjn2aw7+n+A==",
     *     "plain_text": "TheSecretWordWhichSavesMe",
     *     "aad": "AnyPersonalizedTextAddHere",
     *     "enc_algo": "AES"
     * }'
     * @param symmetricEncryption : Should pass symmetric_key, IV, plain_text, enc_algo and aad (only for AES) properties in json string
     * @return : Return cipher text (base 64 encoded):
     * {
     *     "enc_algo": "AES",
     *     "symmetric_key": "tTE23BRNMGiNbYUPB22R66Ha8gaaD0FsaPpTcw2fm2k=",
     *     "IV": "nvsjNGLMK/1Hjn2aw7+n+A==",
     *     "plain_text": "TheSecretWordWhichSavesMe",
     *     "base64_cipher_text": "J10aXKdz2gV2RRrgNrXXSfQVFoEI8fVRln8PpSlsRFGsb3Wasrwjdpo=",
     *     "aad": "AnyPersonalizedTextAddHere"
     * }
     */
    @PostMapping(value = "/encrypt",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    SymmetricEncryption encrypt(@RequestBody SymmetricEncryption symmetricEncryption) {
        if (symmetricEncryption.getEncAlgo().compareToIgnoreCase(CommonConstants.AES_ENC_ALGO) == 0) {
            symmetricEncryption.setBase64EncodedEncryptedCipherText(
                    aesCipherAPI.encrypt(
                            symmetricEncryption.getBase64EncodedKey(),
                            symmetricEncryption.getBase64EncodeIV(),
                            symmetricEncryption.getAad(),
                            symmetricEncryption.getPlainText()
                    )
            );
        } else { // Its ChaCha20-Poly1305
            symmetricEncryption.setBase64EncodedEncryptedCipherText(
                    chaChaCipherAPI.encrypt(symmetricEncryption.getBase64EncodedKey(),
                            symmetricEncryption.getBase64EncodeIV(),
                            symmetricEncryption.getPlainText()
                    )
            );
        }
        return symmetricEncryption;
    }

    /**
     * This endpoint is called to decrypt cipher text to retrieve corresponding plain text. It expects keying material used for encryption and cipher text.
     * Sample request:
     * curl --location --request POST 'http://localhost:8080/crypt/decrypt' \
     * --header 'Content-Type: application/json' \
     * --data-raw '{
     *     "symmetric_key": "tTE23BRNMGiNbYUPB22R66Ha8gaaD0FsaPpTcw2fm2k=",
     *     "IV": "nvsjNGLMK/1Hjn2aw7+n+A==",
     *     "base64_cipher_text": "J10aXKdz2gV2RRrgNrXXSfQVFoEI8fVRln8PpSlsRFGsb3Wasrwjdpo=",,
     *     "enc_algo": "AES",
     *     "aad": "AnyPersonalizedTextAddHere"
     * }'
     *
     * @param symmetricEncryption : Pass in symmetric_key, IV, base64_cipher_text, enc_algo and aad (only for AES encryption)
     * @return : Corresponding plain text:
     * {
     * "plain_text" : "Hello Crypto World!",
     * "enc_algo" : "AES",
     * "IV" : "qVsGLYhOnzBbDUIyTk595w==",
     * "base64_cipher_text" : "OvnpZsO0gzfZ+yRCugFWLSgl5MZmj4VZNX8tf00jCViWk4o=",
     * "aad" : "localhost",
     * "symmetric_key" : "0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y="
     * }
     */
    @PostMapping(value = "/decrypt",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody SymmetricEncryption decrypt(@RequestBody SymmetricEncryption symmetricEncryption) {
        if (symmetricEncryption.getEncAlgo().compareToIgnoreCase(CommonConstants.AES_ENC_ALGO) == 0) {
            symmetricEncryption.setPlainText(
                    aesCipherAPI.decrypt(symmetricEncryption.getBase64EncodedKey(),
                            symmetricEncryption.getBase64EncodeIV(),
                            symmetricEncryption.getAad(),
                            symmetricEncryption.getBase64EncodedEncryptedCipherText()
                    )
            );
        } else { // Its ChaCha20-Poly1305
            symmetricEncryption.setPlainText(
                    chaChaCipherAPI.decrypt(
                            symmetricEncryption.getBase64EncodedKey(),
                            symmetricEncryption.getBase64EncodeIV(),
                            symmetricEncryption.getBase64EncodedEncryptedCipherText()
                    )
            );
        }

        return symmetricEncryption;
    }
}


/**
 * Step 1:
 *
 * curl 'http://localhost:8080/generate-symmetric-key' -X POST -H "Content-Type: application/json" -d '{"key_size":"256","enc_algo":"ChaCha20"}' | json_pp
 * {
 *    "symmetric_key" : "wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=",
 *    "enc_algo" : "ChaCha20",
 *    "key_size" : 256
 * }
 *
 * Step 2:
 *
 * curl 'http://localhost:8080/generate-initialization-vector' -X POST -H "Content-Type: application/json" -d '{"iv_size":"12"}' | json_pp
 * {
 *    "iv_size" : 12,
 *    "IV" : "kLOPGk+d7j5ppaw0"
 * }
 *
 * Step 3:
 *
 * curl 'http://localhost:8080/encrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=","IV":"kLOPGk+d7j5ppaw0","plain_text":"Hello Crypto World!","enc_algo":"ChaCha20"}' | json_pp
 * {
 *    "IV" : "kLOPGk+d7j5ppaw0",
 *    "enc_algo" : "ChaCha20",
 *    "base64_cipher_text" : "OtG5e36uvUfjk7jFEymNSVrvDCsWpPg3Be4gBmuSLjrjd/E=",
 *    "symmetric_key" : "wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=",
 *    "plain_text" : "Hello Crypto World!"
 * }
 *
 * Step 4:
 *
 * curl 'http://localhost:8080/decrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=","IV":"kLOPGk+d7j5ppaw0","base64_cipher_text":"OtG5e36uvUfjk7jFEymNSVrvDCsWpPg3Be4gBmuSLjrjd/E=","enc_algo":"ChaCha20"}' | json_pp
 * {
 *    "symmetric_key" : "wPhxPnRWuVmCyS/Mvak+KHMb2Uan0BgdvTYGMYJbC+M=",
 *    "plain_text" : "Hello Crypto World!",
 *    "IV" : "kLOPGk+d7j5ppaw0",
 *    "enc_algo" : "ChaCha20",
 *    "base64_cipher_text" : "OtG5e36uvUfjk7jFEymNSVrvDCsWpPg3Be4gBmuSLjrjd/E="
 * }
 */
