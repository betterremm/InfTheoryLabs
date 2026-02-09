package by.betterremm.InformationTheory.services;

import org.springframework.stereotype.Service;

@Service
public class PlayfairService {

    public static String encrypt(String key, String text) {
        return key + ":" + text;
    }

    public static String decrypt(String key, String text) {
        return key + ":" + text;
    }
}
