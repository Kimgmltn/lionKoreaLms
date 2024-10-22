package kr.co.lionkorea.utils;

public class Base62 {

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long value){
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62_CHARS.charAt((int) (value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }
}
