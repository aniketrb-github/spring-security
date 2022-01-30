package com.jca.crypto.util;

/**
 * @author Aniket Bharsakale
 */
public class CommonConstants {
    public CommonConstants() {
    }

    public static final String AES_ENC_TRANSFORMATION_STRING = "AES/GCM/NoPadding";
    public static final int TAG_BIT_LENGTH = 128; // Needed for GCM mode
    public static final String DRBG_PERSONALIZED_STRING = "AES_GSM_DRBG_SHA_256_SYMMETRIC_KEY";
    public static final String AES_ENC_ALGO = "AES";
    public static final String DRBG_ENC_ALGO = "DRBG";
    public static final int SECURITY_STRENGTH = 256;

}

