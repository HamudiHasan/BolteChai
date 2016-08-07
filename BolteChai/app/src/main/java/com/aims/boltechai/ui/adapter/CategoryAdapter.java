package com.aims.boltechai.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.activeandroid.util.Log;
import com.aims.boltechai.R;
import com.aims.boltechai.model.ActivityItem;
import com.aims.boltechai.model.Category;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> list;
    private Context context;

    public CategoryAdapter(List<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Category item = list.get(position);

        Log.d("Adapter",item.categoryTitle);
        holder.tvItemTitle.setText(item.categoryTitle);



    }

    @Override
    public int getItemCount() {
        // return the number of items in the list
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView tvItemTitle;


        public ViewHolder(View itemView) {
            super(itemView);

            // get the references of view components here

            itemImage = (ImageView) itemView.findViewById(R.id.activity_image);
            tvItemTitle = (TextView) itemView.findViewById(R.id.activity_title);


        }
    }
}
