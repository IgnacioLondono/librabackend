package com.library.loans.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessRulesValidationDTO {

    private Boolean allRulesValid;
    private String summary;
    private List<RuleValidation> rules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RuleValidation {
        private String ruleName;
        private String description;
        private Boolean valid;
        private String message;
    }
}

