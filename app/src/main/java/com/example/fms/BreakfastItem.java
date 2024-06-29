package com.example.fms;

public class BreakfastItem {
    private String category;
    private String selectedOption;
    private String[] options;
    private int selectedIndex;

    public BreakfastItem(String category, String selectedOption, String[] options) {
        this.category = category;
        this.selectedOption = selectedOption;
        this.options = options;
        this.selectedIndex = 0; // Default to the first option
    }

    public BreakfastItem(String fruit, String chooseAFruit) {
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

    public String[] getOptions() {
        return options;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
