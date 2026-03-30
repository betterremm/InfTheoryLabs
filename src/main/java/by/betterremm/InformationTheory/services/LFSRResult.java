package by.betterremm.InformationTheory.services;

public class LFSRResult {

    private final byte[] outputBytes;  // зашифрованные/расшифрованные байты
    private final String keyBinary;    // ключ LFSR в виде строки 0 и 1

    public LFSRResult(byte[] outputBytes, String keyBinary) {
        this.outputBytes = outputBytes;
        this.keyBinary = keyBinary;
    }

    public byte[] getOutputBytes() {
        return outputBytes;
    }

    public String getKeyBinary() {
        return keyBinary;
    }
}