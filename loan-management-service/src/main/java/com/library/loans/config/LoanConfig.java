package com.library.loans.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "loan")
@Data
public class LoanConfig {

    private Integer defaultDays = 14;
    private Integer maxExtensions = 2;
    private Double finePerDay = 5.0;
}






