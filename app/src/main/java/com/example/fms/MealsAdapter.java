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

    public MealsAdapter(List<MealstItem> mealItems) {
        // Ensure mealItems is not null
        this.mealItems = Objects.requireNonNull(mealItems, "Meal items list cannot be null");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meals_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Ensure the item is not null
        MealstItem item = Objects.requireNonNull(mealItems.get(position), "Meal item at position " + position + " is null");
        holder.tvMealCategory.setText(item.getCategory());

        // Setting up Spinner for selecting options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),
                android.R.layout.simple_spinner_item, Objects.requireNonNull(item.getOptions(), "Options for item " + position + " are null"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);

        // Set default selection
        holder.spinner.setSelection(item.getSelectedIndex());

        // Update selected option when spinner item selected
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setSelectedOption(item.getOptions()[position]);
                item.setSelectedIndex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMealCategory;
        Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealCategory = itemView.findViewById(R.id.tv_meal_category);
            spinner = itemView.findViewById(R.id.spinner_options);
        }
    }
}
