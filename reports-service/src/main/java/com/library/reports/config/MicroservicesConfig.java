package com.library.reports.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "microservices")
@Data
public class MicroservicesConfig {

    private UserManagement userManagement = new UserManagement();
    private BookCatalog bookCatalog = new BookCatalog();
    private LoanManagement loanManagement = new LoanManagement();
    private Notifications notifications = new Notifications();

    @Data
    public static class UserManagement {
        private String url;
    }

    @Data
    public static class BookCatalog {
        private String url;
    }

    @Data
    public static class LoanManagement {
        private String url;
    }

    @Data
    public static class Notifications {
        private String url;
    }
}






