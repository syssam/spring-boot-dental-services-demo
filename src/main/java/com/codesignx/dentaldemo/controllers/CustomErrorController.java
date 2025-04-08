package com.codesignx.dentaldemo.controllers;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);
    
    @RequestMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public String handleError(HttpServletRequest request, Model model) {
        // 获取错误状态码
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            httpStatus = HttpStatus.valueOf(statusCode);
        }
        
        // 记录错误信息
        logger.error("发生错误: {} - {}", httpStatus, request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        
        // 获取异常对象
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (throwable != null) {
            logger.error("错误详情:", throwable);
            model.addAttribute("exception", throwable);
            
            // 获取并格式化堆栈跟踪
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : throwable.getStackTrace()) {
                stackTrace.append(element.toString()).append("\n");
            }
            model.addAttribute("trace", stackTrace.toString());
        }
        
        // 向模型添加错误信息
        model.addAttribute("status", httpStatus.value());
        model.addAttribute("message", httpStatus.getReasonPhrase());
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        model.addAttribute("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        
        // 返回错误页面
        return "error";
    }
    
    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleErrorJson(HttpServletRequest request) {
        // 获取错误状态码
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            httpStatus = HttpStatus.valueOf(statusCode);
        }
        
        // 记录错误信息
        logger.error("API错误: {} - {}", httpStatus, request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", java.time.LocalDateTime.now());
        errorDetails.put("status", httpStatus.value());
        errorDetails.put("error", httpStatus.getReasonPhrase());
        errorDetails.put("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        
        // 获取异常并记录
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (throwable != null) {
            logger.error("API错误详情:", throwable);
            errorDetails.put("message", throwable.getMessage());
            errorDetails.put("exception", throwable.getClass().getName());
            
            // 添加简化的堆栈信息
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            String[] simplifiedTrace = new String[Math.min(10, stackTrace.length)]; // 只取前10行
            for (int i = 0; i < simplifiedTrace.length; i++) {
                simplifiedTrace[i] = stackTrace[i].toString();
            }
            errorDetails.put("trace", simplifiedTrace);
        }
        
        return new ResponseEntity<>(errorDetails, httpStatus);
    }
} 