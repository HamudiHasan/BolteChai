package com.aims.boltechai.ui.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.util.Log;
import com.aims.boltechai.R;
import com.aims.boltechai.model.Category;
import com.aims.boltechai.model.Help;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter for Help Recycle view
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    private List<Help> list;
    private Context context;
    private HelpListener categoryListener;

    public HelpAdapter(List<Help> list, Context context, HelpListener categoryListener) {
        this.list = list;
        this.context = context;
        this.categoryListener = categoryListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_help, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Help item = list.get(position);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check the permission and send SMS
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(item.phone, null, item.name+" i need your help. "+"Please Help me.", null, null);
                    Toast.makeText(v.getContext(), "Sending SMS "+item.phone, Toast.LENGTH_SHORT).show();
                }


            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categoryListener.onDeleteClicked(item.name);
                        new Delete().from(Help.class).where("name =?",item.name).execute();
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setTitle("Are you Sure ?");
                builder.setMessage("Do you really want to Delete?");
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        if (item.image != null && !item.image.isEmpty()) {
            File f = new File(item.image);
            Picasso.with(context).load(f).
                    into(holder.ivProfile);
        }

        holder.tvName.setText(item.name);
        holder.tvNumber.setText(item.phone);
    }

    @Override
    public int getItemCount() {
        // return the number of items in the list
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View container;
        CircleImageView ivProfile;
        TextView tvName;
        TextView tvNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.item_help_container);
            ivProfile = (CircleImageView) itemView.findViewById(R.id.item_help_image);
            tvName = (TextView) itemView.findViewById(R.id.item_help_title);
            tvNumber = (TextView) itemView.findViewById(R.id.item_help_number);

        }
    }
    // Help click event listener
    public interface HelpListener {
        void onDeleteClicked(String name);
    }
}
