import by.betterremm.InformationTheory.services.VigenereService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VigenereServiceTest {

    @Test
    void smokeTestWithInvalidChars() {
        VigenereService cipher = new VigenereService();

        String text = "ПРИВЕТ, МИР! 123"; // недопустимые символы
        String key = "КЛЮЧ";

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty(), "Шифротекст не должен быть пустым");

        String decrypted = cipher.decrypt(encrypted, key);
        // Сравниваем только валидные буквы
        assertEquals("ПРИВЕТМИР", decrypted);
    }

    @Test
    void handleLetterYo() {
        VigenereService cipher = new VigenereService();

        String text = "ЁЖИК"; // текст с буквой Ё
        String key = "КЛЮЧ";

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = cipher.decrypt(encrypted, key);
        assertEquals("ЁЖИК", decrypted);
    }

    @Test
    void invalidKeyContainsSymbols() {
        VigenereService cipher = new VigenereService();

        String text = "ПРИВЕТ";
        String key = "123!@#"; // полностью невалидный ключ

        String encrypted = cipher.encrypt(text, key);
        assertEquals("", encrypted, "Шифротекст должен быть пустым для невалидного ключа");

        String decrypted = cipher.decrypt(encrypted, key);
        assertEquals("", decrypted);
    }

    @Test
    void partiallyInvalidKey() {
        VigenereService cipher = new VigenereService();

        String text = "ПРИВЕТ";
        String key = "КЛЮЧ123"; // ключ содержит допустимые и недопустимые символы

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty(), "Шифротекст формируется по допустимым символам ключа");

        String decrypted = cipher.decrypt(encrypted, key);
        // Должны восстановиться только буквы текста
        assertEquals("ПРИВЕТ", decrypted);
    }

    @Test
    void emptyOrNullInputs() {
        VigenereService cipher = new VigenereService();

        assertEquals("", cipher.encrypt("", "КЛЮЧ"));
        assertEquals("", cipher.encrypt(null, "КЛЮЧ"));
        assertEquals("", cipher.encrypt("ПРИВЕТ", ""));
        assertEquals("", cipher.encrypt("ПРИВЕТ", null));

        assertEquals("", cipher.decrypt("", "КЛЮЧ"));
        assertEquals("", cipher.decrypt(null, "КЛЮЧ"));
        assertEquals("", cipher.decrypt("ПРИВЕТ", ""));
        assertEquals("", cipher.decrypt("ПРИВЕТ", null));
    }

    @Test
    void repeatedEncryptionDecryption() {
        VigenereService cipher = new VigenereService();

        String text = "ПРИВЕТМИР";
        String key = "КЛЮЧ";

        // Шифруем несколько раз подряд
        String encrypted = cipher.encrypt(text, key);
        String encrypted2 = cipher.encrypt(encrypted, key);

        String decrypted = cipher.decrypt(encrypted2, key);
        String decryptedOriginal = cipher.decrypt(decrypted, key);

        assertEquals(text, decryptedOriginal);
    }
}
