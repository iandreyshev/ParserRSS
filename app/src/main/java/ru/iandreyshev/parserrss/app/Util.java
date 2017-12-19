package ru.iandreyshev.parserrss.app;

public class Util {
    public static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return null;
        }
    }
}
