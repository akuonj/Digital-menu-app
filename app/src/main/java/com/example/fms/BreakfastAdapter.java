package com.example.fms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalfood.R;

import java.util.List;

public class BreakfastAdapter extends RecyclerView.Adapter<BreakfastAdapter.ViewHolder> {

    private List<BreakfastItem> breakfastItems;

    public BreakfastAdapter(List<BreakfastItem> breakfastItems) {
        this.breakfastItems = breakfastItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakfast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BreakfastItem item = breakfastItems.get(position);
        holder.tvBreakfastCategory.setText(item.getCategory());
        holder.tvSelectedOption.setText(item.getSelectedOption());
    }

    @Override
    public int getItemCount() {
        return breakfastItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBreakfastCategory;
        TextView tvSelectedOption;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBreakfastCategory = itemView.findViewById(R.id.tv_breakfast_category);
            tvSelectedOption = itemView.findViewById(R.id.tv_selected_option);
        }
    }
}
