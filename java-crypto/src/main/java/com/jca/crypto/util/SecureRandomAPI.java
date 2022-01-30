package com.jca.crypto.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.DrbgParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static java.security.DrbgParameters.Capability.PR_AND_RESEED;

/**
 * @author Aniket Bharsakale
 */
@Slf4j
@Component
public class SecureRandomAPI {

    /***
     * Configure SecureRandom object, with most secure configurations. Using NIST approved DRBG mechanism
     * (SHA-256 based), 256 bit security  strength, prediction resistant and reseeding option.
     * @return DRBG mechanism based, 256 bit security strength, prediction resistant  and reseeding complaint SecureRandom object
     */
    public SecureRandom drbgSecureRandom() {
        SecureRandom drbgSecureRandom = null;
        try {
            drbgSecureRandom = SecureRandom.getInstance(CommonConstants.DRBG_ENC_ALGO, // Uses default configured DRBG mechanism which is usually SHA-256 Hash. Good idea to check in your java.security file.
                    DrbgParameters.instantiation(CommonConstants.SECURITY_STRENGTH, // Security strength, default is 128 as configured in java.security
                            PR_AND_RESEED, // prediction resistance, and re-seeding support.
                            // Prediction Resistance ==  compromise of the DRBG internal state has no effect on the security of future DRBG outputs.
                            // Reseeding == Periodic reseeding, to avoid too many output from a seed
                            CommonConstants.DRBG_PERSONALIZED_STRING.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            log.error("DRBG algorithm for generating CSPRNG is not supported");
        }

        return drbgSecureRandom;
    }
}