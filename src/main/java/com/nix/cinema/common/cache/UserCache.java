package com.nix.cinema.common.cache;

import com.nix.cinema.model.MemberModel;

import javax.servlet.http.HttpSession;

/**
 * @author Kiss
 * @date 2018/05/01 20:08
 */
public final class UserCache {
    public final static String USER_SESSION_KEY = "user_id";
    //本地线程临时存储
    private final static ThreadLocal<HttpSession> local = new ThreadLocal<>();

    public static MemberModel currentUser() {
        return (MemberModel) local.get().getAttribute(USER_SESSION_KEY);
    }
    public static void putUser(HttpSession session) {
        local.set(session);
    }
    public static HttpSession getSession() {
        return local.get();
    }
}
