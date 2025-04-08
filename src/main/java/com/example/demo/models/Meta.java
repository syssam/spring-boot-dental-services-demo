package com.example.demo.models;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.http.HttpServletRequest;

public class Meta {
    private String title;
    private String description;
    private String image;
    private String type;
    private String url;

    public Meta(String title, String description, String image, String type, String url) {
        this.title = title != null && !title.isEmpty() ? title + " - " + getApplicationName() : getApplicationName();
        this.description = description;
        this.image = image;
        this.type = type != null ? type : "website";
        this.url = url != null ? url : getCurrentUrl();
    }
    
    public Meta(String title, String description, String image) {
        this(title, description, image, "website", null);
    }
    
    public Meta(String title, String description) {
        this(title, description, null);
    }

    public Meta(String description) {
        this(null, description, null);
    }

    private String getApplicationName() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            Environment env = WebApplicationContextUtils
                .getRequiredWebApplicationContext(request.getServletContext())
                .getEnvironment();
            return env.getProperty("spring.application.name", "Application");
        } catch (Exception e) {
            return "Application";
        }
    }

    private String getCurrentUrl() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            StringBuffer url = request.getRequestURL();
            return url.toString();
        } catch (Exception e) {
            // If we can't get the current URL for any reason, return empty string
            return "";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }       

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
