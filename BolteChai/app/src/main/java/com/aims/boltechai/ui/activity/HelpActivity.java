package com.aims.boltechai.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.activeandroid.query.Select;
import com.aims.boltechai.R;
import com.aims.boltechai.model.Help;
import com.aims.boltechai.ui.adapter.HelpAdapter;
import com.aims.boltechai.ui.fragment.ImageDialogFragment;
import com.aims.boltechai.util.AppUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Controllers the Help pages Action & events
 */
public class HelpActivity extends AppCompatActivity implements HelpAdapter.HelpListener {

    View addHelp;
    public HelpAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        addHelp = findViewById(R.id.layout_help_add_new);

        List<Help> helpItems = new ArrayList<Help>();
        RecyclerView rvHelp = (RecyclerView) findViewById(R.id.rv_help);
        GridLayoutManager gridLayout;

        // Reads from Database
        List<Help> item = new Select().from(Help.class).execute();
        helpItems.addAll(item);

        adapter = new HelpAdapter(helpItems, this, this);

        gridLayout = new GridLayoutManager(this, 1);
        rvHelp.setLayoutManager(gridLayout);
        rvHelp.setItemAnimator(new DefaultItemAnimator());
        rvHelp.setAdapter(adapter);

        if (getRoleName().equals("Child")) {
            addHelp.setVisibility(View.GONE);
        }
        addHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newHelp();
            }
        });
    }

    public String getRoleName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString(AppUtils.BOLTE_CHAI_ROLE, AppUtils.MODE_PARENT);
    }

    // add new help contact
    public void newHelp() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_add_help);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText etName = (EditText) dialog.findViewById(R.id.et_help_add_name);
        final EditText etPhone = (EditText) dialog.findViewById(R.id.et_help_add_phone);

        final String[] photo = {""};
        View takePhoto = dialog.findViewById(R.id.layout_add_image_help);

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel_add_help);
        Button btnSave = (Button) dialog.findViewById(R.id.btn_save_help);
        final ImageView imagePreview = (ImageView) dialog.findViewById(R.id.iv_help_thumblin);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogFragment imageDialogFragment = new ImageDialogFragment();
                imageDialogFragment.setOnPhotoSelectionFromGalleryListener(new ImageDialogFragment.OnPhotoSelectionFromGalleryListener() {
                    @Override
                    public void onPhotoSelect(DialogFragment tag, Uri uri) {

                        imagePreview.setVisibility(View.VISIBLE);
                        Picasso.with(getApplicationContext()).load(uri).into(imagePreview);
                        final File f = AppUtils.saveImageFile(uri, "image_" + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());
                        photo[0] = f.getPath() + "";
                    }
                });
                imageDialogFragment.show(getSupportFragmentManager(), "image");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(etName) && !isEmpty(etPhone)) {
                    Help help = new Help(etName.getText().toString(), etPhone.getText().toString(), photo[0]);
                    help.save();
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (dialog != null)
            dialog.show();
    }

    // check EditText empty or not
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }

    @Override
    public void onDeleteClicked(String name) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
