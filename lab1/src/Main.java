import java.io.*;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        if (args.length < 4)
            return;

        AffineCipher affineCipher =
                new AffineCipher(AffineCipher.AlphabetType.LAT_LOW);                                                    ReportMaker.newChapter();

        int keyA = inputInteger("[MAIN] Input the key A: ");
        int keyB = inputInteger("[MAIN] Input the key B: ");                                                        ReportMaker.newChapter();

        cryptTextFromFile(args[0], affineCipher::encrypt, keyA, keyB);                                                  ReportMaker.newChapter();
        cryptTextFromFile(args[1], affineCipher::decrypt, keyA, keyB);                                                  ReportMaker.newChapter();



        AffineCipher affineCipherFA =
                new AffineCipher(AffineCipher.AlphabetType.CYR_LOW,
                        AffineCipher.AlphabetType.CYR_UP,
                        AffineCipher.AlphabetType.LAT_LOW,
                        AffineCipher.AlphabetType.LAT_UP,
                        AffineCipher.AlphabetType.DIGITS);                                                              ReportMaker.newChapter();

        keyA = inputInteger("[MAIN] Input the key A: ");
        keyB = inputInteger("[MAIN] Input the key B: ");                                                            ReportMaker.newChapter();

        cryptTextFromFile(args[2], affineCipherFA::encryptSerial, keyA, keyB);                                          ReportMaker.newChapter();
        cryptTextFromFile(args[3], affineCipherFA::decryptSerial, keyA, keyB);                                          ReportMaker.newChapter();
    }

    private static int inputInteger (String text) {
        int result;
        System.out.print(text);

        Scanner scanner = new Scanner(System.in);
        result = scanner.nextInt();
        return result;
    }


    private static LinkedList<String> getLinesFromFile (String path) {
        LinkedList<String> result = new LinkedList<>();

        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine())
                result.add(scanner.nextLine());
        } catch (FileNotFoundException e) {
            System.err.printf("[MAIN] Error! File [%s] not found!\n", path);
            return null;
        }

        return result;
    }

    private static void addLinesToFile (String path, List<String> lines) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, false)))) {
            lines
                    .forEach(pw::println);
        } catch (IOException e) {
            System.err.printf("[MAIN] Error! File [%s] not found!\n", path);
//            e.printStackTrace();
        }
    }


    @FunctionalInterface
    private interface Crypter {
        String crypt(String text, Integer keyA, Integer keyB);
    }

    private static void cryptTextFromFile (String path, Crypter crypter, int keyA, int keyB) {
        LinkedList<String> lines =  getLinesFromFile(path);
        List<String> newlines = new LinkedList<>();

        if (null == lines) {
            return;
        }

        lines.forEach(line -> {
                    String newLine = crypter.crypt(line, keyA, keyB);
                    if (null == newLine) return;
                    newlines.add(newLine);
                    System.out.println();
                });

        addLinesToFile(path.substring(0, path.indexOf(".")) + "__translated" + path.substring(path.indexOf(".")), newlines);
    }
}
