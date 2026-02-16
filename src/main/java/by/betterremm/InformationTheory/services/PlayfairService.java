package by.betterremm.InformationTheory.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

@Service("playfair")
public class PlayfairService implements CipherService {

    private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ"; // без J

    @Override
    public String encrypt(String text, String key) {
        if (text == null || key == null) return "";

        text = filter(text.toUpperCase());
        key = filter(normalize(key.toUpperCase()));

        if (text.isEmpty() || key.isEmpty()) return "";

        char[][] table = buildTable(key);
        List<String> pairs = prepareText(text);

        StringBuilder result = new StringBuilder();
        for (String p : pairs) {
            result.append(processPair(p, table, true));
        }
        return result.toString();
    }

    @Override
    public String decrypt(String text, String key) {
        if (text == null || key == null) return "";

        text = filter(normalize(text.toUpperCase()));
        key = filter(normalize(key.toUpperCase()));

        if (text.isEmpty() || key.isEmpty()) return "";
        if (text.length() % 2 != 0) return "";

        char[][] table = buildTable(key);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            result.append(processPair(text.substring(i, i + 2), table, false));
        }

        return result.toString();
    }


    private static String normalize(String s) {
        return s.replace('J', 'I');
    }

    private static String filter(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray())
            if (ALPHABET.indexOf(c) >= 0)
                sb.append(c);
        return sb.toString();
    }

    private static char[][] buildTable(String key) {
        LinkedHashSet<Character> set = new LinkedHashSet<>();
        for (char c : key.toCharArray())
            set.add(c);
        for (char c : ALPHABET.toCharArray())
            set.add(c);

        char[][] table = new char[5][5];
        Iterator<Character> it = set.iterator();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                table[i][j] = it.next();
        return table;
    }

    private static List<String> prepareText(String text) {
        List<String> pairs = new ArrayList<>();
        for (int i = 0; i < text.length(); ) {
            char a = text.charAt(i);
            char b = (i + 1 < text.length()) ? text.charAt(i + 1) : 'X';

            if (a == b) {
                pairs.add("" + a + 'X');
                i++;
            } else {
                pairs.add("" + a + b);
                i += 2;
            }
        }

        // если последняя пара длины 1, добавляем X
        if (!pairs.isEmpty() && pairs.get(pairs.size() - 1).length() == 1)
            pairs.set(pairs.size() - 1, pairs.get(pairs.size() - 1) + "X");

        return pairs;
    }

    private static String processPair(String pair, char[][] t, boolean enc) {
        int[] p1 = find(pair.charAt(0), t);
        int[] p2 = find(pair.charAt(1), t);
        if (p1 == null || p2 == null) return "";

        if (p1[0] == p2[0]) { // строка
            int shift = enc ? 1 : 4;
            return "" + t[p1[0]][(p1[1] + shift) % 5] + t[p2[0]][(p2[1] + shift) % 5];
        }

        if (p1[1] == p2[1]) { // столбец
            int shift = enc ? 1 : 4;
            return "" + t[(p1[0] + shift) % 5][p1[1]] + t[(p2[0] + shift) % 5][p2[1]];
        }

        // прямоугольник
        return "" + t[p1[0]][p2[1]] + t[p2[0]][p1[1]];
    }

    private static int[] find(char c, char[][] t) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (t[i][j] == c)
                    return new int[]{i, j};
        return null;
    }
}
