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
public class PermissionInterceptor implements HandlerInterceptor,PermissionHandler<RoleInterfaceModel,Method> {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MemberModel user = (MemberModel) request.getSession().getAttribute(UserCache.USER_SESSION_KEY);
        UserCache.putUser(request.getSession());
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (methodIsPermission(method)) {
                Clear methodClear = method.getAnnotation(Clear.class);
                Clear controllerClear = method.getDeclaringClass().getAnnotation(Clear.class);
                //如果method没有标识为清除权限校验
                if (methodClear == null && controllerClear == null) {
                    boolean ok;
                    if (user != null) {
                        ok = userPermission(user,method);
                    } else {
                        //如果用户未登录
                        AdminController adminController = method.getDeclaringClass().getAnnotation(AdminController.class);
                        if (adminController != null) {
                            response.sendRedirect("/admin");
                        } else {
                            response.sendRedirect("/member/login");
                        }
                        return false;
                    }
                    //如果用户权限不足
                    if (!ok) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "权限不足");
                        return ok;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 判断该方法是否需要拦截权限
     * */
    private boolean methodIsPermission(Method method) {
        MemberController memberController = method.getDeclaringClass().getAnnotation(MemberController.class);
        AdminController adminController = method.getDeclaringClass().getAnnotation(AdminController.class);
        if (memberController != null || adminController != null) {
            return true;
        }
        return false;
    }

    private boolean userPermission(MemberModel user, Method method) {
        if (MemberService.ADMIN_USERNAME.equals(user.getUsername())) {
            return true;
        }
        RoleModel role = user.getRole();
        List<RoleInterfaceModel> roleInterfaces = role.getRoleInterfaces();
        for (RoleInterfaceModel roleInterface:roleInterfaces) {
            if (isHavePermission(roleInterface,method)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isHavePermission(RoleInterfaceModel roleInterface, Method method) {
        if (!roleInterface.getEnabled()) {
            return false;
        }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        RequestMapping controllerRequestMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
        String[] controllerUrls = controllerRequestMapping != null ? controllerRequestMapping.value() : null;
        String[] methodUrls = postMapping != null ? postMapping.value() : getMapping.value();
        for (int i = 0;methodUrls != null && i < methodUrls.length;i ++) {
            String url = "";
            if (controllerUrls != null) {
                for (int j = 0;j < controllerUrls.length; j ++) {
                    url = controllerUrls[j] + methodUrls[i];
                    if (url.matches(roleInterface.getUrl().replaceAll("\\*\\*","\\.\\*"))) {
                        return true;
                    }
                }
            } else {
                url = methodUrls[i];
                if (url.matches(roleInterface.getUrl().replaceAll("\\*\\*","\\.\\*"))) {
                    return true;
                }
            }
            System.out.println(url);
        }
        return false;
    }
}
