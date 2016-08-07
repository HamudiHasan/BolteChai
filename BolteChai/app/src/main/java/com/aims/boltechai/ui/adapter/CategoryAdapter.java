package com.aims.boltechai.ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.activeandroid.util.Log;
import com.aims.boltechai.R;
import com.aims.boltechai.model.Category;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> list;
    private Context context;
    private CategoryListener categoryListener;

    public CategoryAdapter(List<Category> list, Context context, CategoryListener categoryListener) {
        this.list = list;
        this.context = context;
        this.categoryListener = categoryListener;
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

        Log.d("Adapter", item.categoryTitle);
        holder.tvItemTitle.setText(item.categoryTitle);

        if (item.categoryImage != null && !item.categoryImage.isEmpty()) {
            File f = new File(item.categoryImage);
            Picasso.with(context).load(f).
                    into(holder.itemImage);
        }
        if (item.categoryAudio == null || item.categoryAudio.isEmpty()) {
            holder.itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryListener.onCategoryClicked(item.getId());
                }
            });
        }
        if (item.categoryAudio != null && !item.categoryAudio.isEmpty()) {
            holder.itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mp = new MediaPlayer();

                    try {
                        mp.setDataSource(item.categoryAudio + File.separator);
                        mp.prepare();
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        holder.itemImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(context, "Looooong Presss", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // add condition if no audion then go to new
        // if audio then play .

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

    public interface CategoryListener {
        void onCategoryClicked(long id);
    }
}
