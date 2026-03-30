package by.betterremm.InformationTheory.services;

public class LFSRService implements CipherService {

    @Override
    public String encrypt(String text, String register) {
        byte[] reg = filterToBitArray(register);
        StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {
            int encryptedChar = 0;

            for (int i = 0; i < 8; i++) {
                int textBit = (c >> (7 - i)) & 1;

                int keyBit = reg[0]; // выходящий бит

                int newBit = reg[0] ^ reg[19] ^ reg[20] ^ reg[33];

                shiftLeft(reg, newBit);

                int encryptedBit = textBit ^ keyBit;
                encryptedChar = (encryptedChar << 1) | encryptedBit;
            }

            sb.append((char) encryptedChar);
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, String register) {
        return encrypt(text, register); // XOR симметричен
    }

    private byte[] filterToBitArray(String text) {
        byte[] bits = new byte[34];
        int index = 0;

        for (char c : text.toCharArray()) {
            if ((c == '0' || c == '1') && index < 34) {
                bits[index++] = (byte)(c - '0');
            }
        }

        return bits;
    }

    private void shiftLeft(byte[] reg, int newBit) {
        for (int i = 0; i < reg.length - 1; i++) {
            reg[i] = reg[i + 1];
        }
        reg[reg.length - 1] = (byte)newBit;
    }



}
