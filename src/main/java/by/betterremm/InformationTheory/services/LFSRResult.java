package by.betterremm.InformationTheory.services;

public class LFSRResult {

    private final byte[] outputBytes;
    private final String keyBinary;

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