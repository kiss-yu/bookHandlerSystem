package com.nix.cinema.common.annotation;

import java.lang.annotation.*;

/**
 * @author Kiss
 * @date 2018/05/02 11:48
 */
@Target( {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemberController {
}
