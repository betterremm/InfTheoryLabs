import by.betterremm.InformationTheory.services.PlayfairService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayfairServiceTest {

    @Test
    void smokeTestWithInvalidChars() {
        PlayfairService cipher = new PlayfairService();

        String text = "HELLO, WORLD! 123"; // недопустимые символы
        String key = "SECRET";

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty(), "Шифротекст не должен быть пустым");
        assertEquals("ISKYIQEWFQKC", encrypted);

        String decrypted = cipher.decrypt(encrypted, key);
        // Фильтруем исходный текст, чтобы сравнить только валидные буквы
        assertEquals("HELXLOWORLDX", decrypted);
    }

    @Test
    void oddLengthText() {
        PlayfairService cipher = new PlayfairService();

        String text = "TESTING"; // длина 7 → нечётная
        String key = "KEY";

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = cipher.decrypt(encrypted, key);
        assertEquals("TESTINGX", decrypted);
    }

    @Test
    void evenLength(){
        PlayfairService cipher = new PlayfairService();

        String text = "KILKER"; // длина 6 → чётная
        String key = "KEY";

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = cipher.decrypt(encrypted, key);
        assertEquals("KILKER", decrypted);
    }
    @Test
    void evenLengthButRepeatedLetterText() {
        PlayfairService cipher = new PlayfairService();

        String text = "KILLER"; // длина 6 → чётная
        String key = "KEY";

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = cipher.decrypt(encrypted, key);
        assertEquals("KILXLERX", decrypted);
    }

    @Test
    void repeatedLetters() {
        PlayfairService cipher = new PlayfairService();

        String text = "BALLOON"; // подряд две одинаковые буквы: L и O
        String key = "SECRET";

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = cipher.decrypt(encrypted, key);
        // Должны восстановиться только валидные буквы без добавочного X
        assertEquals("BALXLOON", decrypted);
    }

    @Test
    void invalidTextAllSymbols() {
        PlayfairService cipher = new PlayfairService();

        String text = "123456!@#"; // полностью невалидный текст
        String key = "SECRET";

        String encrypted = cipher.encrypt(text, key);
        assertEquals("", encrypted, "Шифротекст должен быть пустым для полностью невалидного текста");

        String decrypted = cipher.decrypt(encrypted, key);
        assertEquals("", decrypted);
    }

    @Test
    void multipleConsecutiveLettersX() {
        PlayfairService cipher = new PlayfairService();

        String text = "XXHELLOXX"; // подряд несколько X
        String key = "SECRET";

        String encrypted = cipher.encrypt(text, key);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = cipher.decrypt(encrypted, key);
        assertEquals("XXXHELLOXXXX", decrypted);
    }

    @Test
    void emptyOrNullInputs() {
        PlayfairService cipher = new PlayfairService();

        assertEquals("", cipher.encrypt("", "KEY"));
        assertEquals("", cipher.encrypt(null, "KEY"));
        assertEquals("", cipher.encrypt("HELLO", ""));
        assertEquals("", cipher.encrypt("HELLO", null));

        assertEquals("", cipher.decrypt("", "KEY"));
        assertEquals("", cipher.decrypt(null, "KEY"));
        assertEquals("", cipher.decrypt("HELLO", ""));
        assertEquals("", cipher.decrypt("HELLO", null));
    }
}
