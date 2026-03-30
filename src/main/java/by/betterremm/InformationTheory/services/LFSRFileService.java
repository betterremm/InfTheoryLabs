package by.betterremm.InformationTheory.services;

import java.util.ArrayList;
import java.util.List;

public class LFSRFileService {

    private final int REGISTER_SIZE = 34;

    // Полином x^34 + x^15 + x^14 + x + 1
    private final int[] TAPS = {0, 19, 20, 33};

    public byte[] process(byte[] inputBytes, String register) {
        byte[] reg = normalizeRegister(register);
        List<Byte> output = new ArrayList<>();

        StringBuilder keyStreamBits = new StringBuilder();

        for (byte b : inputBytes) {
            byte processedByte = 0;
            for (int i = 7; i >= 0; i--) {
                int inputBit = (b >> i) & 1;
                int keyBit = reg[0]; // выход LFSR
                keyStreamBits.append(keyBit);

                int newBit = 0;
                for (int tap : TAPS) {
                    newBit ^= reg[tap];
                }

                shiftLeft(reg, newBit);
                processedByte = (byte)((processedByte << 1) | (inputBit ^ keyBit));
            }
            output.add(processedByte);
        }

        // вывод ключевого потока (для визуализации)
        System.out.println("Generated Key (binary):");
        System.out.println(keyStreamBits.toString());

        byte[] result = new byte[output.size()];
        for (int i = 0; i < output.size(); i++) {
            result[i] = output.get(i);
        }

        return result;
    }

    private byte[] normalizeRegister(String input) {
        byte[] reg = new byte[REGISTER_SIZE];
        int index = 0;
        for (char c : input.toCharArray()) {
            if ((c == '0' || c == '1') && index < REGISTER_SIZE) {
                reg[index++] = (byte)(c - '0');
            }
        }
        while (index < REGISTER_SIZE) {
            reg[index++] = 0;
        }
        return reg;
    }

    private void shiftLeft(byte[] reg, int newBit) {
        for (int i = 0; i < reg.length - 1; i++) {
            reg[i] = reg[i + 1];
        }
        reg[reg.length - 1] = (byte)newBit;
    }

    public LFSRResult processWithKey(byte[] input, String registerState) {
        StringBuilder keyBuilder = new StringBuilder();
        byte[] output = new byte[input.length];

        // копия регистра
        long reg = 0;
        for (char c : registerState.toCharArray()) {
            reg <<= 1;
            reg |= (c == '1') ? 1 : 0;
        }

        // LFSR: x^34 + x^15 + x^14 + x + 1
        for (int i = 0; i < input.length; i++) {
            byte b = input[i];
            byte outByte = 0;

            for (int j = 7; j >= 0; j--) {
                int regOut = (int)((reg >> 33) & 1); // старший бит регистра
                keyBuilder.append(regOut);
                int bit = ((b >> j) & 1) ^ regOut; // XOR с входным битом
                outByte |= bit << j;

                // считаем новый бит по многочлену: x^34 + x^15 + x^14 + x + 1
                int newBit = (int) (((reg >> 33) ^ (reg >> 14) ^ (reg >> 13) ^ (reg >> 0)) & 1);
                reg = ((reg << 1) & 0x3FFFFFFFFL) | newBit; // 34-битный регистр
            }

            output[i] = outByte;
        }

        return new LFSRResult(output, keyBuilder.toString());
    }


}