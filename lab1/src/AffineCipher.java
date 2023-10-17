import java.util.ArrayList;
import java.util.List;

public class AffineCipher {
    private final ArrayList<Character> ALPHABET;
    private final int M;


    public enum AlphabetType {
        LAT_LOW,
        LAT_UP,
        CYR_LOW,
        CYR_UP,
        DIGITS
    }



    public AffineCipher (AlphabetType ... alphabetTypes) {
        this.ALPHABET = new ArrayList<>();

        for (AlphabetType type : alphabetTypes) {
            switch (type) {
                case LAT_LOW -> {
                    for (char a = 'a'; a <= 'z'; a++) ALPHABET.add(a);
                }
                case LAT_UP -> {
                    for (char a = 'A'; a <= 'Z'; a++) ALPHABET.add(a);
                }
                case CYR_LOW -> {
                    for (char a = 'а'; a <= 'е'; a++) ALPHABET.add(a);
                    ALPHABET.add('ё');
                    for (char a = 'ж'; a <= 'я'; a++) ALPHABET.add(a);
                }
                case CYR_UP -> {
                    for (char a = 'А'; a <= 'Е'; a++) ALPHABET.add(a);
                    ALPHABET.add('Ё');
                    for (char a = 'Ж'; a <= 'Я'; a++) ALPHABET.add(a);
                }
                case DIGITS -> {
                    for (char a = '0'; a <= '9'; a++) ALPHABET.add(a);

                }
            }
        }

        ReportMaker.printLine("INIT", "ALPHABET", ALPHABET);
        ReportMaker.printLine(null,null,ALPHABET.stream().map(ALPHABET::indexOf).toList());
        this.M = ALPHABET.size();
    }



    private int E(int x, int a, int b) {
        return (a*x + b) % M;
    }

    private int D(int x, int a, int b) {
        return ((x+ M -b)*a) % M;
    }


    public boolean areCoprime(int digit1, int digit2) {
        int lowerDigit = Math.abs(Math.min(digit1, digit2));
        int greaterDigit = Math.abs(Math.max(digit1, digit2));

        if (lowerDigit == 0 || greaterDigit == 0)
            return false;

        if (greaterDigit % lowerDigit == 0 && lowerDigit != 1)
            return false;

        for (int i = 2; i < lowerDigit; i++) {
            if (lowerDigit % i == 0 && greaterDigit % i == 0)
                return false;
        }

        return true;
    }

    private int getModularMultiplicativeInverse (int x, int modular) {
        int result = 0;
        for (int i = 1; i <= modular; i++) {
            if (x*i % modular == 1) {
                result = i;
                break;
            }
        }
        return result;
    }


    public String encrypt(String text, int keyA, int keyB) {
        /* STEP 0 */
        if (null == text) {
            System.err.println("[ERROR] [ENCRYPT] Input text is NULL");
            return null;
        }

        if (!areCoprime(keyA, M)) {
            System.err.printf("[ERROR] [ENCRYPT] " +
                    "Key A [%d] and alphabet size M [%d] are NOT comprime!\n", keyA, ALPHABET.size());
            return null;
        }

        /* STEP 1 */
        List<Integer> indexes = text.chars()
                .mapToObj(o -> (char) o)
                .filter(ALPHABET::contains)
                .map(ALPHABET::indexOf)
                .toList();

        /* STEP 2 */
        List<Integer> encryptedIndexes = indexes.stream()
                .map(index -> E(index, keyA, keyB))
                .toList();

        /* STEP 3 */
        StringBuilder stringBuilder = new StringBuilder();
        encryptedIndexes.stream()
                .map(ALPHABET::get)
                .forEach(stringBuilder::append);
        String cipherText = stringBuilder.toString();

        ReportMaker.printLine("ENCRYPTION", "OPEN TEXT", text);
        ReportMaker.printLine("ENCRYPTION", "LETTERS -> INDEXES", indexes);
        ReportMaker.printLine("ENCRYPTION", "ENCRYPTED INDEXES", encryptedIndexes);
        ReportMaker.printLine("ENCRYPTION", "CIPHER TEXT", cipherText);

        return cipherText;
    }

    public String decrypt(String text, int keyA, int keyB) {
        /* STEP 0 */
        if (null == text) {
            System.err.println("[ERROR] [DECRYPT] Input text is NULL");
            return null;
        }

        if (!areCoprime(keyA, M)) {
            System.err.printf("[ERROR] [DECRYPT] " +
                    "Key A [%d] and alphabet size M [%d] are NOT comprime!\n", keyA, ALPHABET.size());
            return null;
        }

        /* STEP 1 */
        List<Integer> indexes = text.chars()
                .mapToObj(o -> (char) o)
                .filter(ALPHABET::contains)
                .map(ALPHABET::indexOf)
                .toList();;

        /* STEP 2 */
        int keyAModularMultiplicativeInverse = getModularMultiplicativeInverse(keyA, M);
        List<Integer> decryptedIndexes = indexes.stream()
                .map(index -> D(index, keyAModularMultiplicativeInverse, keyB))
                .toList();

        /* STEP 3 */
        StringBuilder stringBuilder = new StringBuilder();
        decryptedIndexes
                .forEach(index -> stringBuilder.append(ALPHABET.get(index)));
        String openText = stringBuilder.toString();

        ReportMaker.printLine("DECRYPTION", "CIPHER TEXT", text);
        ReportMaker.printLine("DECRYPTION", "LETTERS -> INDEXES", indexes);
        ReportMaker.printLine("DECRYPTION", "DECRYPTED LETTERS", decryptedIndexes);
        ReportMaker.printLine("DECRYPTION", "OPEN TEXT", openText);

        return openText;
    }


    public String decryptSerial (String text, int keyA, int keyB) {
        StringBuilder stringBuilder = new StringBuilder();

        /* STEP 0 */
        if (null == text) {
            System.err.println("[ERROR] [DECRYPT_SERIAL] Input text is NULL");
            return null;
        }

        if (!areCoprime(keyA, M)) {
            System.err.printf("[ERROR] [DECRYPT_SERIAL] " +
                    "Key A [%d] and alphabet size M [%d] are NOT comprime!\n", keyA, ALPHABET.size());
            return null;
        }

        /* STEP 1 - 2 - 3 */
        int keyAModularMultiplicativeInverse = getModularMultiplicativeInverse(keyA, M);

        text.chars()
                .mapToObj(o -> (char) o)
                .map(c -> {
                    if (ALPHABET.contains(c)) {
                        int index = ALPHABET.indexOf(c);

                        int decryptedIndex = D(index, keyAModularMultiplicativeInverse, keyB);
                        return ALPHABET.get(decryptedIndex);
                    } else {
                        return c;
                    }
                })
                .forEach(stringBuilder::append);
        String cipherText = stringBuilder.toString();

        ReportMaker.printLine("DECRYPTION", "CIPHER TEXT", text);
        ReportMaker.printLine("DECRYPTION", "OPEN TEXT", cipherText);

        return cipherText;
    }

    public String encryptSerial (String text, int keyA, int keyB) {
        StringBuilder stringBuilder = new StringBuilder();

        /* STEP 0 */
        if (null == text) {
            System.err.println("[ERROR] [ENCRYPT_SERIAL] Input text is NULL");
            return null;
        }

        if (!areCoprime(keyA, M)) {
            System.err.printf("[ERROR] [ENCRYPT_SERIAL] " +
                    "Key A [%d] and alphabet size M [%d] are NOT comprime!\n", keyA, ALPHABET.size());
            return null;
        }

        /* STEP 1 - 2 - 3 */
        text.chars()
                .mapToObj(o -> (char) o)
                .map(c -> {
                    if (ALPHABET.contains(c)) {
                        int index = ALPHABET.indexOf(c);
                        int encryptedIndex = E(index, keyA, keyB);
                        return ALPHABET.get(encryptedIndex);
                    } else {
                        return c;
                    }
                })
                .forEach(stringBuilder::append);
        String cipherText = stringBuilder.toString();

        ReportMaker.printLine("ENCRYPTION", "OPEN TEXT", text);
        ReportMaker.printLine("ENCRYPTION", "CIPHER TEXT", cipherText);

        return cipherText;
    }

}

