package com.bookstore.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Report class for locator validation results
 */
public class LocatorValidationReport {

    private int totalValid;
    private int totalInvalid;
    private final Map<String, PageValidationResult> pageResults;

    public LocatorValidationReport() {
        this.pageResults = new HashMap<>();
    }

    public void addPageResult(String pageName, int validCount, int invalidCount,
                             List<String> validLocators, List<String> invalidLocators) {
        PageValidationResult result = new PageValidationResult();
        result.setPageName(pageName);
        result.setValidCount(validCount);
        result.setInvalidCount(invalidCount);
        result.setValidLocators(new ArrayList<>(validLocators));
        result.setInvalidLocators(new ArrayList<>(invalidLocators));

        pageResults.put(pageName, result);
    }

    public void setTotalValid(int totalValid) {
        this.totalValid = totalValid;
    }

    public void setTotalInvalid(int totalInvalid) {
        this.totalInvalid = totalInvalid;
    }

    public int getTotalValid() {
        return totalValid;
    }

    public int getTotalInvalid() {
        return totalInvalid;
    }

    public int getTotalLocators() {
        return totalValid + totalInvalid;
    }

    public double getSuccessRate() {
        if (getTotalLocators() == 0) return 0.0;
        return (double) totalValid / getTotalLocators() * 100.0;
    }

    public Map<String, PageValidationResult> getPageResults() {
        return pageResults;
    }

    public boolean hasFailures() {
        return totalInvalid > 0;
    }

    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LOCATOR VALIDATION SUMMARY ===\n");
        sb.append(String.format("Total Locators: %d\n", getTotalLocators()));
        sb.append(String.format("Valid Locators: %d\n", totalValid));
        sb.append(String.format("Invalid Locators: %d\n", totalInvalid));
        sb.append(String.format("Success Rate: %.2f%%\n", getSuccessRate()));
        sb.append("\n");

        for (PageValidationResult result : pageResults.values()) {
            sb.append(String.format("📄 %s: %d valid, %d invalid\n",
                     result.getPageName(), result.getValidCount(), result.getInvalidCount()));
        }

        return sb.toString();
    }

    public String getDetailedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(getSummary());
        sb.append("\n=== DETAILED RESULTS ===\n\n");

        for (PageValidationResult result : pageResults.values()) {
            sb.append(String.format("📄 %s\n", result.getPageName()));
            sb.append("----------------------------------------\n");

            if (!result.getValidLocators().isEmpty()) {
                sb.append("✅ VALID LOCATORS:\n");
                for (String locator : result.getValidLocators()) {
                    sb.append("  ✓ ").append(locator).append("\n");
                }
                sb.append("\n");
            }

            if (!result.getInvalidLocators().isEmpty()) {
                sb.append("❌ INVALID LOCATORS:\n");
                for (String locator : result.getInvalidLocators()) {
                    sb.append("  ✗ ").append(locator).append("\n");
                }
                sb.append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Inner class to hold page-specific validation results
     */
    public static class PageValidationResult {
        private String pageName;
        private int validCount;
        private int invalidCount;
        private List<String> validLocators;
        private List<String> invalidLocators;

        // Getters and setters
        public String getPageName() { return pageName; }
        public void setPageName(String pageName) { this.pageName = pageName; }

        public int getValidCount() { return validCount; }
        public void setValidCount(int validCount) { this.validCount = validCount; }

        public int getInvalidCount() { return invalidCount; }
        public void setInvalidCount(int invalidCount) { this.invalidCount = invalidCount; }

        public List<String> getValidLocators() { return validLocators; }
        public void setValidLocators(List<String> validLocators) { this.validLocators = validLocators; }

        public List<String> getInvalidLocators() { return invalidLocators; }
        public void setInvalidLocators(List<String> invalidLocators) { this.invalidLocators = invalidLocators; }

        public double getPageSuccessRate() {
            int total = validCount + invalidCount;
            if (total == 0) return 0.0;
            return (double) validCount / total * 100.0;
        }
    }
}
