package com.nix.cinema.util;


import com.nix.cinema.common.Pageable;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 11723
 */
public final class ReturnUtil {
    private final static int SUCCESS_CODE = 1;
    private final static String SUCCESS_MSG = "SUCCESS";
    private final static int FAIL_CODE = -1;
    private final static String FAIL_MSG = "FAIL";

    public static ReturnObject success() {
        return success(null);
    }

    /**
     * 接口调用成功，不需要
     * @param o 返回的内容
     * */
    public static ReturnObject<Object> success(Object o) {
        return success(null,o);
    }
    /**
     * 接口调用成功，有提示消息
     * @param msg 提示消息
     * @param o 返回内容
     * */
    public static ReturnObject<Object> success(String msg, Object o) {
        return success(msg,o,null);
    }
    public static ReturnObject<Object> success(String msg, Object o, Map map) {
        ReturnObject<Object> object = new ReturnObject<>();
        object.setStatus(SUCCESS_CODE);
        object.setMsg(msg == null ? SUCCESS_MSG : msg);
        object.setData(o);
        object.setAdditionalData(map);
        return object;
    }

    public static ReturnObject fail() {
        return fail(null);
    }

    /**
     * 接口调用失败
     * @param o 失败时返回的内容
     * */
    public static ReturnObject<Object> fail(Object o) {
        return fail(null,null,o);
    }

    /**
     * 接口调用失败，有提示消息
     * @param msg 提示消息
     * @param o 失败返回内容
     * */
    public static ReturnObject<Object> fail(Integer code, String msg, Object o) {
        return fail(code,msg,o,null);
    }
    public static ReturnObject<Object> fail(Integer code, String msg, Object o,Map map) {
        ReturnObject<Object> object = new ReturnObject<>();
        object.setStatus(code == null ? FAIL_CODE : code);
        object.setMsg(msg == null ? FAIL_MSG : msg);
        object.setData(o);
        object.setAdditionalData(map);
        return object;
    }


    public static ReturnObject list(Pageable pageable, BaseService service) {
        Map additionalData = new HashMap();
        List list = pageable.getList(service);
        additionalData.put("total",pageable.getCount());
        return success(null,list,additionalData);
    }
}
