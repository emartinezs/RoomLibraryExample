package com.example.alejandro.roomexampleproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alejandro.roomexampleproject.R;
import com.example.alejandro.roomexampleproject.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private List<Category> categories;
    private Context context;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_cardview, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryTextView.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTextView;

        public CategoryViewHolder(View view) {
            super(view);
            categoryTextView = view.findViewById(R.id.categoryTextView);
        }
    }
}
