package net.slipp.myslipp.web;

import net.slipp.myslipp.domain.User;

import javax.servlet.http.HttpSession;

//중복을 제거하기 위한 util class
public class HttpSessionUtils {
    public static final String USER_SESSION_KEY = "session_user";

    //로그인 여부 확인
    public static boolean isLoginUser(HttpSession session){
        Object session_user = session.getAttribute(USER_SESSION_KEY);
        if (session_user == null){
            return false;
        }

        return true;
    }

    //session user 정보를 조회
    public static User getUserFromSession(HttpSession session){
        if (!isLoginUser((session))){
            return null;
        }

        //한줄로 줄이면
        //return (User)session.getAttribute(USER_SESSION_KEY);

        User session_user = (User)session.getAttribute(USER_SESSION_KEY);
        return session_user;
    }
}
