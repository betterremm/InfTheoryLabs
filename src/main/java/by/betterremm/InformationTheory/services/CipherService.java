package by.betterremm.InformationTheory.services;

public interface CipherService {
    public String encrypt(String text, String key);
    public String decrypt(String text, String key);
}
