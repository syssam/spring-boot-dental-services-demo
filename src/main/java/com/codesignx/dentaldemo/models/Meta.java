package com.codesignx.dentaldemo.models;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private String title;
    private String description;
    private String image;
    private String type;
    private String url;
    private String siteName;

    public Meta(String title, String description, String image, String type, String url) {
        this.siteName = getApplicationName();
        this.title = title != null && !title.isEmpty() ? title + " - " + siteName : siteName;
        this.description = description;
        this.image = image;
        this.type = type != null && !type.isEmpty() ? type : "website";
        this.url = url != null && !url.isEmpty() ? url : getCurrentUrl();
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

    public Meta() {
        this(null, null, null);
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
}
