package com.example.fms;

public class MealstItem {
    private String category;
    private String selectedOption;
    private String[] options;
    private int selectedIndex = 0; // Default to 0

    public MealstItem(String category, String selectedOption, String[] options) {
        this.category = category;
        this.selectedOption = selectedOption;
        this.options = options;
        this.selectedIndex = getIndexOfOption(selectedOption);
    }

    private int getIndexOfOption(String option) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(option)) {
                return i;
            }
        }
        return 0; // Default index if not found
    }

    public String getCategory() {
        return category;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public String[] getOptions() {
        return options;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
        this.selectedIndex = getIndexOfOption(selectedOption);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex >= 0 && selectedIndex < options.length) {
            this.selectedIndex = selectedIndex;
            this.selectedOption = options[selectedIndex];
        }
    }
}
