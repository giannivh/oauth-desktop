package com.giannivanhoecke.oauth.desktop.extension;

import com.giannivanhoecke.oauth.desktop.exception.PkceException;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class Pkce {

    public static final String CODE_CHALLENGE_METHOD = "S256";

    private static final int PKCE_CODE_VERIFIER_MAX_LENGTH = 128;
    private static final char[] ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            .toCharArray();
    private static final String CODE_CHALLENGE_ALGORITHM = "SHA-256";

    private final String codeVerifier;
    private final String codeChallenge;

    private Pkce(String codeVerifier, String codeChallenge) {
        this.codeVerifier = codeVerifier;
        this.codeChallenge = codeChallenge;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public String getCodeChallenge() {
        return codeChallenge;
    }

    public static Pkce generate() {
        try {
            String codeVerifier = createCodeVerifier();
            String codeChallenge = createCodeChallenge(codeVerifier);
            return new Pkce(codeVerifier, codeChallenge);
        } catch (Exception e) {
            throw new PkceException("Could not generate PKCE", e);
        }
    }

    // util

    private static String createCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        char[] buffer = new char[PKCE_CODE_VERIFIER_MAX_LENGTH];
        for (int i = 0; i < buffer.length; ++i) {
            buffer[i] = ALPHANUMERIC[secureRandom.nextInt(ALPHANUMERIC.length)];
        }
        return new String(buffer);
    }

    private static String createCodeChallenge(String codeVerifier)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(CODE_CHALLENGE_ALGORITHM);
        md.update(codeVerifier.getBytes(StandardCharsets.ISO_8859_1));
        return Base64.encodeBase64URLSafeString(md.digest());
    }
}
