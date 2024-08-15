package com.example.fms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {

    private List<MealstItem> mealItems;
    private OnItemSelectedListener onItemSelectedListener;

    // Arrays for prices
    private final double[] fruitPrices = {1.5, 2.0};
    private final double[] cerealPrices = {3.0, 3.5, 4.0, 2.5};
    private final double[] starchPrices = {5.0, 1.5, 1.8, 1.6, 1.9};
    private final double[] meatPrices = {3.5};
    private final double[] spreadsPrices = {0.5, 0.7, 0.8};

    public MealsAdapter(List<MealstItem> mealItems) {
        this.mealItems = Objects.requireNonNull(mealItems, "Meal items list cannot be null");
    }

    // Define the listener interface
    public interface OnItemSelectedListener {
        void onItemSelected();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meals_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealstItem item = Objects.requireNonNull(mealItems.get(position), "Meal item at position " + position + " is null");
        holder.tvMealCategory.setText(item.getCategory());

        // Setting up Spinner for selecting options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),
                android.R.layout.simple_spinner_item, Objects.requireNonNull(item.getOptions(), "Options for item " + position + " are null"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);

        // Set default selection
        holder.spinner.setSelection(item.getSelectedIndex());

        // Update selected option when spinner item is selected
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setSelectedOption(item.getOptions()[position]);
                item.setSelectedIndex(position);

                double price = getPrice(item.getCategory(), position);
                holder.tvMealPrice.setText(String.format("$%.2f", price));

                // Notify listener that an item has been selected
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set initial price
        double initialPrice = getPrice(item.getCategory(), item.getSelectedIndex());
        holder.tvMealPrice.setText(String.format("$%.2f", initialPrice));
    }

    @Override
    public int getItemCount() {
        return mealItems.size();
    }

    // Method to get price based on category and index
    private double getPrice(String category, int index) {
        switch (category) {
            case "Fruit":
                return fruitPrices[index];
            case "Cereal":
                return cerealPrices[index];
            case "Starch":
                return starchPrices[index];
            case "Meat":
                return meatPrices[index];
            case "Spreads":
                return spreadsPrices[index];
            default:
                return 0.0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMealCategory;
        Spinner spinner;
        TextView tvMealPrice; // Added TextView for price

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealCategory = itemView.findViewById(R.id.tv_meal_category);
            spinner = itemView.findViewById(R.id.spinner_options);
            tvMealPrice = itemView.findViewById(R.id.tv_meal_price); // Initialize the price TextView
        }
    }
}
