package com.nix.cinema.common.supper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nix.cinema.common.PermissionHandler;
import com.nix.cinema.common.annotation.AdminController;
import com.nix.cinema.common.annotation.Clear;
import com.nix.cinema.common.annotation.MemberController;
import com.nix.cinema.common.cache.UserCache;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.model.RoleInterfaceModel;
import com.nix.cinema.model.RoleModel;
import com.nix.cinema.service.impl.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/01 20:07
 * 权限管理
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor,PermissionHandler<RoleModel,String> {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    private final static String[] IGNORE_RESOURCES_PATH = {"**/login.html"};
    private final static String[] HANDLER_RESOURCES_PATH = {"**.html"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MemberModel user = (MemberModel) request.getSession().getAttribute(UserCache.USER_SESSION_KEY);
        UserCache.putUser(request.getSession());
        String uri = request.getRequestURI();
        System.out.println("uri==" + uri);
        boolean ok = true;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (methodIsPermission(method)) {
                Clear methodClear = method.getAnnotation(Clear.class);
                Clear controllerClear = method.getDeclaringClass().getAnnotation(Clear.class);
                //如果method没有标识为清除权限校验
                if (methodClear == null && controllerClear == null) {
                    if (user != null) {
                        ok = isHavePermission(user.getRole(),uri);
                    } else {
                        ok = false;
                    }
                }
            }
        } else {
            if (!isIgnorePath(uri) && uri.matches(".*.html")) {
                if (user != null) {
                    ok = isHavePermission(user.getRole(),uri);
                }else {
                    ok = false;
                }
            }
        }
        if (!ok) {
            if (uri.matches("/admin.*")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/member/login");
            }
        }
        return ok;
    }

    /**
     * 判断该方法是否需要拦截权限
     * */
    private boolean methodIsPermission(Method method) {
        MemberController memberController = method.getDeclaringClass().getAnnotation(MemberController.class);
        AdminController adminController = method.getDeclaringClass().getAnnotation(AdminController.class);
        if (memberController != null || adminController != null) {
            return false;
        }
        return false;
    }

    /**
     * 判断是否能够执行目的权限
     *
     * @param r 持有的权限
     * @param m        目的权限
     * @return 持有权限是否能够操作目的权限
     */
    @Override
    public boolean isHavePermission(RoleModel r, String m) {
        if (RoleModel.ADMIN_VALUE.equals(r.getValue())) {
            return true;
        }
        if (r != null) {
            for (RoleInterfaceModel roleInterfaceModel:r.getRoleInterfaces()) {
                if (m.matches(roleInterfaceModel.getUrl().replaceAll("\\*\\*","\\.\\*"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isIgnorePath(String uri) {
        for (String path:IGNORE_RESOURCES_PATH) {
            if (uri.matches(path.replaceAll("\\*\\*","\\.\\*"))) {
                return true;
            }
        }
        for (String path:HANDLER_RESOURCES_PATH) {
            if (uri.matches(path.replaceAll("\\*\\*","\\.\\*"))) {
                return false;
            }
        }
        return true;
    }
}
