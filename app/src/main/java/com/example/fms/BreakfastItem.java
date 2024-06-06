package com.example.fms;

public class BreakfastItem {
    private String category;
    private String selectedOption;

    public BreakfastItem(String category, String selectedOption) {
        this.category = category;
        this.selectedOption = selectedOption;
    }

    public String getCategory() {
        return category;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
}
