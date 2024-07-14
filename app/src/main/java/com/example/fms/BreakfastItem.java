package com.example.fms;

import java.util.Objects;

public class BreakfastItem {
    private String category;
    private String selectedOption;
    private String[] options;
    private int selectedIndex;

    public BreakfastItem(String category, String selectedOption, String[] options) {
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.selectedOption = selectedOption; // Allow null for selected option
        this.options = Objects.requireNonNull(options, "Options cannot be null");
        this.selectedIndex = 0; // Default to the first option
    }

    // Remove the empty constructor if not used
    public BreakfastItem(String fruit, String chooseAFruit) {
        this(fruit, chooseAFruit, new String[]{}); // Initialize with an empty array
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
