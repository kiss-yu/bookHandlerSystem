package com.nix.cinema.common.annotation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @author Kiss
 * @date 2018/05/02 11:47
 */
@Target( {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
public @interface AdminController {
}
