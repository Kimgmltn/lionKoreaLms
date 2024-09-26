package kr.co.lionkorea.utils;

import jakarta.servlet.http.Cookie;

public class CommonUtils {

    public static Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        // Cookie 생명주기
        cookie.setMaxAge(24*60*60);
        // Cookie https 통신 진행시
//        cookie.setSecure(true);
        // Cookie 적용할 범위
//        cookie.setPath("/");
        // 프론트에서 js로 접근 못하도록 설정
        cookie.setHttpOnly(true);
        return cookie;
    }
}
