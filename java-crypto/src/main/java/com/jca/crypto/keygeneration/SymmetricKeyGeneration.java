package com.jca.crypto.keygeneration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Aniket Bharsakale
 */
@Slf4j
@Component
public class SymmetricKeyGeneration {

    /***
     * Returns a Base64 encoded Symmetric Key of provided algorithm and key size.
     * @param encryptionAlgo supported encrypted algorithm
     * @param symmetricKeySize required key size.
     * @param secRandom Provided configured instance of SecureRandom
     * @return base64 encoded version of generated symmetric key
     */
    public String generateSymmetricKey(String encryptionAlgo, int symmetricKeySize, SecureRandom secRandom) {
        SecretKey secretKey = null;
        KeyGenerator keyGenerator = null;
        try {
            // Preparing Key generation object
            keyGenerator = KeyGenerator.getInstance(encryptionAlgo);
        } catch (NoSuchAlgorithmException e) {
            log.error(encryptionAlgo + "is not supported");
        }
        keyGenerator.init(symmetricKeySize, secRandom);

        // Key generation
        secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}