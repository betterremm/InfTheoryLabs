package by.betterremm.InformationTheory.services;

import org.springframework.stereotype.Service;

@Service("vigenere")
public class VigenereService implements CipherService {
    private static final String ALPHABET = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final int N = ALPHABET.length();


    @Override
    public String encrypt(String text, String key) {
        if (text == null || key == null) return "";

        text = filter(text.toUpperCase());
        key = filter(key.toUpperCase());

        if (text.isEmpty() || key.isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        char[] currentKey = key.toCharArray();

        for (int i = 0, k = 0; i < text.length(); i++, k = (k + 1) % currentKey.length) {
            int pi = ALPHABET.indexOf(text.charAt(i));
            int ki = ALPHABET.indexOf(currentKey[k]);

            int ci = (pi + ki) % N;
            result.append(ALPHABET.charAt(ci));
            currentKey[k] = ALPHABET.charAt((ki + 1) % N);
        }

        return result.toString();
    }

    @Override
    public String decrypt(String text, String key) {
        if (text == null || key == null) return "";

        text = filter(text.toUpperCase());
        key = filter(key.toUpperCase());

        if (text.isEmpty() || key.isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        char[] currentKey = key.toCharArray();

        for (int i = 0, k = 0; i < text.length(); i++, k = (k + 1) % currentKey.length) {
            int ci = ALPHABET.indexOf(text.charAt(i));
            int ki = ALPHABET.indexOf(currentKey[k]);

            int pi = (ci - ki + N) % N;
            result.append(ALPHABET.charAt(pi));
            currentKey[k] = ALPHABET.charAt((ki + 1) % N);
        }

        return result.toString();
    }

    private static String filter(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray())
            if (ALPHABET.indexOf(c) >= 0)
                sb.append(c);
        return sb.toString();
    }
}
