package com.capitalone.dmsl.catalog.bulk_util;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private int records;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    AsyncContext asyncContext = request.startAsync();
asyncContext.start(() -> {
    try {
        HttpServletRequest req = (HttpServletRequest) asyncContext.getRequest(); // safe reference
        HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();

        // work...
    } finally {
        asyncContext.complete();
    }
});

------------

    import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class AsyncDispatcherConfig {

    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<>(dispatcherServlet, "/");
        registration.setAsyncSupported(true); // ✅ Enable async
        return registration;
    }
}


}
