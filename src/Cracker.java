package com.samples.tkhadir.src;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

class Cracker implements Runnable {

    private int start;
    private int end;
    private final MessageDigest digest = MessageDigest.getInstance("SHA-256");
    private static boolean DONE = false;
    private String found;

    public Cracker(int s, int e) throws NoSuchAlgorithmException {
        start = s;
        end = e;
    }

    public void generate(StringBuilder sb, int n) {
        if (DONE)
            return;

        if (n == sb.length()) {
            String candidate = sb.toString();
            // check password
            byte[] bytes = digest.digest(candidate.getBytes());

            if (Arrays.equals(bytes, PasswordCracker.BYTES_SHA_256_TO_FIND)) {
                found = candidate;
                DONE = true;
            }

            return;
        }

        for (int i = 0; i < PasswordCracker.ALPHABET.length && !DONE; i++) {
            char letter = PasswordCracker.ALPHABET[i];
            sb.setCharAt(n, letter);
            generate(sb, n + 1);
        }

    }

    @Override
    public void run() {

        for (int length = start; length <= end && !DONE; length++) {
            StringBuilder sb = new StringBuilder();
            sb.setLength(length);
            generate(sb, 0);
        }

        if (DONE && found != null && !found.trim().isEmpty()) {
            long duration = System.currentTimeMillis() - PasswordCracker.START_TIME;
            System.out.println("Password cracked in " + TimeUnit.MILLISECONDS.toSeconds(duration) + "." + TimeUnit.MILLISECONDS.toMillis(duration) + " sec. Password was = " + found);
        } else {
            System.out.println("Password not cracked for subset [" + start + ", " + end + "]");
        }
    }

}
