package com.grandp.data.hasher;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHash implements PasswordEncoder {
    private static final PasswordHash HASHER = new PasswordHash();

    private static final int SALT_LENGTH;
    private static final int ITERATIONS;
    private static final int KEY_LENGTH = 256;
    private static final String SECRET_KEY_ALG = "PBKDF2WithHmacSHA256";

    static { // stoinostite se zarejdat ot faila kato Property obekti (ime:stoinost)
        SALT_LENGTH = PasswordHashHelper.saltSize;
        ITERATIONS = PasswordHashHelper.iterations;
    }

    private PasswordHash() {

    }

    public static PasswordHash getInstanceSingleton() {
        return HASHER;
    }

    public static void main(String[] args) {
        PasswordHash ph = new PasswordHash();

		String hashed = ph.encode("121219027");
		System.out.println(hashed);
		long systime1 = System.currentTimeMillis();
		System.out.println(ph.matches("121219027", hashed));
		System.out.println(System.currentTimeMillis() - systime1);
	}
    
    public String encode(CharSequence password) {
        byte[] salt = generateSalt();
        byte[] hash = generateHash(password.toString().toCharArray(), salt);
        return ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }
    public boolean matches(CharSequence password, String hashedPassword) {
        String[] parts = hashedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
        byte[] testHash = generateHash(password.toString().toCharArray(), salt, iterations);
        return slowEquals(hash, testHash);
    }
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    private static byte[] generateHash(char[] password, byte[] salt) {
        return generateHash(password, salt, ITERATIONS);
    }
    private static byte[] generateHash(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(SECRET_KEY_ALG);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

}