package com.magenta.sc;

import java.util.Collection;
import java.util.Map;


public final class EchoUtils {

    public final static int MESSAGE_LENGTH = 60;
    public final static int MAX_WORD_LENGTH = 20;
    public final static int MESSAGE_HEIGHT = 10;


    public static boolean isEmpty(Object s) {
        if (s == null) {
            return true;
        }
        if (s instanceof Collection) {
            return ((Collection) s).isEmpty();
        }
        if (s instanceof Map) {
            return ((Map) s).isEmpty();
        }
        if (s instanceof Object[]) {
            return ((Object[]) s).length == 0;
        }
        return isEmpty(s.toString());
    }

    public static boolean isEmpty(String value) {
        return StringHelper.isEmpty(value);
    }


    public static String formatDialogMessage(String s, String separator) {
        s = removeDuplicateSpaces(s);
        return formatDialogMessage(s, separator, true);
    }

    public static String formatDialogMessage(String s, String separator, boolean norm) {
        s = removeDuplicateSpaces(s);
        return norm ? normString(s, separator, MESSAGE_HEIGHT) : s;
    }

    public static String formatDialogMessage(String s, String separator, Integer maxStrCount) {
        s = removeDuplicateSpaces(s);
        return normString(s, separator, maxStrCount);
    }

    public static String removeDuplicateSpaces(String s) {
        if (isEmpty(s)) {
            return s;
        }
        s = s.replaceAll("\n", " ");
        s = s.replaceAll("\r", " ");
        while (s.contains("  ")) {
            s = s.replaceAll("  ", " ");
        }
        return s.trim();
    }


    public static String normString(String s, String separator, int maxStrCount) {
        String result = "";
        String temp = "";
        String word = "";
        int count = 0;
        while (!isEmpty(s) && count < maxStrCount) {
            s = s.trim();
            if (s.indexOf(" ") > 0) {
                word = s.substring(0, s.indexOf(" ") + 1);
                s = s.substring(s.indexOf(" ") + 1, s.length());
                if (word.length() > MAX_WORD_LENGTH) {
                    word = word.substring(0, MAX_WORD_LENGTH - 3) + "...";
                }
                if ((word.length() + temp.length()) > MESSAGE_LENGTH) {
                    result += temp + separator + word;
                    temp = "";
                    count++;
                } else {
                    temp += word;
                }
            } else {
                if (s.length() > MAX_WORD_LENGTH) {
                    s = s.substring(0, MAX_WORD_LENGTH - 3) + "...";
                }
                if (result.isEmpty() && !temp.isEmpty()) {
                    result = temp;
                }

                if (!result.contains(temp) && !temp.isEmpty()) {
                    result += " " + temp;
                }
                return result + s;
            }
        }
        if (!result.contains(temp) && !temp.isEmpty()) {
            result += " " + temp;
        }
        if (!isEmpty(s) && !result.contains(s)) {
            result += s.length() > MAX_WORD_LENGTH ? s.substring(0, MAX_WORD_LENGTH - 3) + "..." : " " + s;
        }
        return result;
    }

    public static String formatMultiParagraphDialogMessage(String s) {
        s = addParagraphs(removeDuplicateSpaces(s));
        StringBuilder sb = new StringBuilder();

        while (s.contains("\n")) {
            String substring = s.substring(0, s.indexOf("\n") + 1);
            sb.append(substring);
            s = s.substring(s.indexOf("\n") + 1, s.length());
        }
        sb.append(s);
        return sb.toString();
    }

    public static String addParagraphs(String s) {
        return s.replaceAll("\\. ", ".\n");
    }

}
