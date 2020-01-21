package com.ctw.audit.util;

import com.ctw.audit.model.bean.UserDetailsImpl;
import com.ctw.audit.model.enumaration.RolesEnum;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {

    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static String getId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) principal).getId();
        }
        return principal.toString();
    }

    public static boolean hasRole(RolesEnum role) {
        return new WebSecurityExpressionRoot().hasRole(role.name());
    }

    public static boolean hasAnyRole(RolesEnum... roles) {
        String[] strings = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            strings[i] = roles[i].name();
        }
        return new WebSecurityExpressionRoot().hasAnyRole(strings);
    }

    private static class WebSecurityExpressionRoot extends SecurityExpressionRoot {
        private WebSecurityExpressionRoot() {
            super(SecurityContextHolder.getContext().getAuthentication());
        }
    }
}
