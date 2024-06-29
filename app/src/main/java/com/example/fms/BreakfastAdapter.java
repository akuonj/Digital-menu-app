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

import com.example.fms.R;

import java.util.List;

public class BreakfastAdapter extends RecyclerView.Adapter<BreakfastAdapter.ViewHolder> {

    private List<BreakfastItem> breakfastItems;

    public BreakfastAdapter(List<BreakfastItem> breakfastItems) {
        this.breakfastItems = breakfastItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakfast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BreakfastItem item = breakfastItems.get(position);
        holder.tvBreakfastCategory.setText(item.getCategory());

        // Setting up Spinner for selecting options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),
                android.R.layout.simple_spinner_item, item.getOptions());
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
        return breakfastItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBreakfastCategory;
        Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBreakfastCategory = itemView.findViewById(R.id.tv_breakfast_category);
            spinner = itemView.findViewById(R.id.spinner_options);
        }
    }
}
