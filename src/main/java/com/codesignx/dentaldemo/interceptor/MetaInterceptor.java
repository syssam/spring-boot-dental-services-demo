package com.codesignx.dentaldemo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.codesignx.dentaldemo.models.Meta;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Component
public class MetaInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) {
        if (modelAndView != null) {
            modelAndView.addObject("meta", new Meta());
        }
    }
} 