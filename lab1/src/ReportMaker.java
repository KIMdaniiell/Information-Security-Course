
public class ReportMaker {
    private final static int LINE_WIDTH = 100;
    private final static int REPORT_TITLE_WIDTH = 13;
    private final static int REPORT_INFO_WIDTH = 22;
    private final static int REPORT_CELL_WIDTH = 4;

    private final static String DELIMITER = "*";

    private static String getCellSpacesAddition (String content) {
        int contentLength = content.length();

        if (content.length() > REPORT_CELL_WIDTH) {
            System.out.printf("--- is too long [%s]\n", content);
            return "";
        }

        return " ".repeat(REPORT_CELL_WIDTH - contentLength);
    }

    private static String getTitleSpacesAddition (String content) {
        int contentLength = content.length();

        if (content.length() > REPORT_TITLE_WIDTH) {
            System.out.printf("--- is too long [%s]\n", content);
            return "";
        }

        return " ".repeat(REPORT_TITLE_WIDTH - contentLength);
    }

    private static String getInfoSpacesAddition (String content) {
        int contentLength = content.length();

        if (content.length() > REPORT_INFO_WIDTH) {
            System.out.printf("--- is too long [%s]\n", content);
            return "";
        }

        return " ".repeat(REPORT_INFO_WIDTH - contentLength);
    }

    public static String alignWidth (String content) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c :content.toCharArray()) {
                stringBuilder.append(c);
                stringBuilder.append(getCellSpacesAddition(c+""));
        }

        return stringBuilder.toString().strip();
    }

    public static String alignWidth (Iterable<?> iterable) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object o :iterable) {
            String content = o.toString();
            stringBuilder.append(content);
            stringBuilder.append(getCellSpacesAddition(content));
        }

        return stringBuilder.toString().strip();
    }

    public static void printLine(String prefix, String info, String value) {
        if (null == prefix || prefix.isEmpty()) {
            prefix = "";
        } else {
            prefix = "[%s]".formatted(prefix);
        }

        if (null == info || info.isEmpty()) {
            info = "";
        } else {
            info = "%s :".formatted(info);
        }

        System.out.printf("%s%s%s%s[%s]\n",
                prefix,
                getTitleSpacesAddition(prefix),
                info,
                getInfoSpacesAddition(info),
                alignWidth(value));
    }

    public static void printLine(String prefix, String info, Iterable<?> iterable) {
        if (null == prefix || prefix.isEmpty()) {
            prefix = "";
        } else {
            prefix = "[%s]".formatted(prefix);
        }

        if (null == info || info.isEmpty()) {
            info = "";
        } else {
            info = "%s :".formatted(info);
        }

        System.out.printf("%s%s%s%s[%s]\n",
                prefix,
                getTitleSpacesAddition(prefix),
                info,
                getInfoSpacesAddition(info),
                alignWidth(iterable));
    }

    public static void newChapter() {
        System.out.println("\n" + DELIMITER.repeat(LINE_WIDTH) + "\n");
    }

}


