package com.aims.boltechai.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aims.boltechai.R;
import com.aims.boltechai.model.Category;
import com.aims.boltechai.ui.fragment.AudioDialogFragment;
import com.aims.boltechai.ui.fragment.ImageDialogFragment;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Zakir on 07/08/2016.
 */
public class DialogUtils {


    public static final String SELECT_FROM_DOCUMENTS = "Select from documents";
    public static final String RECORD_NEW_AUDIO = "Record new Audio";

    public static void showDialog(final Context context, final CategoryDialogListener categoryDialogListener, final int parentId) {
        if (context != null) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_add_new_main_catagory);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            final Category category = new Category();

            final EditText etCategoryTitle = (EditText) dialog.findViewById(R.id.et_category_title_new_category);

            final View audioLayout = dialog.findViewById(R.id.layout_audio_new_category);
            final ImageView imagePreview = (ImageView) dialog.findViewById(R.id.iv_photo_preview);
            final LinearLayout layoutAddImage = (LinearLayout) dialog.findViewById(R.id.layout_add_image);
            final LinearLayout layoutAddAudio = (LinearLayout) dialog.findViewById(R.id.layout_add_audio);

            //final ImageView ivCategoryImage = (ImageView) dialog.findViewById(R.id.iv_category_image_new_category);

            Button btnSubmit = (Button) dialog.findViewById(R.id.btn_add_new_category);
            Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel_new_category);

            layoutAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog.dismiss();
    //                categoryDialogListener.onImageButtonClicked();
                    ImageDialogFragment imageDialogFragment = new ImageDialogFragment();
                    imageDialogFragment.setOnPhotoSelectionFromGalleryListener(new ImageDialogFragment.OnPhotoSelectionFromGalleryListener() {
                        @Override
                        public void onPhotoSelect(DialogFragment tag, Uri uri) {
                            Toast.makeText(context, uri.getPath(), Toast.LENGTH_LONG).show();
                            imagePreview.setVisibility(View.VISIBLE);
                            Picasso.with(context).load(uri).into(imagePreview);
                            final File f = AppUtils.saveImageFile(uri, "image_" + parentId + "_" + SystemClock.uptimeMillis() + ".jpg", context);
                            category.categoryImage = f.getPath();
                        }
                    });
                    imageDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "image");
                }
            });

            layoutAddAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AudioDialogFragment audioDialogFragment = new AudioDialogFragment();
                    audioDialogFragment.setOnAudioSelectionFromGalleryListener(new AudioDialogFragment.OnAudioSelectionFromGalleryListener() {
                        @Override
                        public void onAudioSelect(DialogFragment tag, Uri uri) {
                            Toast.makeText(context, uri.getPath(), Toast.LENGTH_LONG).show();

                           final File f = AppUtils.saveAudioFile(uri, "audio_" + parentId + "_" + SystemClock.uptimeMillis() + ".mp3", context);
                            category.categoryAudio = f.getPath();
                        }
                    });
                    audioDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "audio");
                  /*  DialogUtils.getAudioPickerDialog(context
                            , new AudioItemSelectedListener() {
                                @Override
                                public void onItemSelected(String item) {
                                    if (item.equals(DialogUtils.SELECT_FROM_DOCUMENTS)) {
                                        // TODO: 07/08/2016 select audio from documents

                                    } else if (item.equals(DialogUtils.RECORD_NEW_AUDIO)) {
                                        //// TODO: 07/08/2016 hamudi create our own recorder and store the file
                                        //// // TODO: 07/08/2016 save the path to category and database
                                    }
                                }
                            }).show();*/
    //                categoryDialogListener.onAudioButtonClicked();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //category = new Category(1, etCategoryTitle.getText().toString(), "Hasan");
                    category.parentId = parentId;
                    category.categoryTitle = etCategoryTitle.getText().toString();
                    dialog.dismiss();
                    categoryDialogListener.onSaveButtonClicked(category);

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
    }


    public static AlertDialog.Builder getAudioPickerDialog(Context context, final AudioItemSelectedListener listener) {
        final String[] items = {SELECT_FROM_DOCUMENTS, RECORD_NEW_AUDIO};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                listener.onItemSelected(items[position]);
            }
        });
        return builder;
    }

    public interface CategoryDialogListener {
        void onAudioButtonClicked();

        void onImageButtonClicked();

        void onSaveButtonClicked(Category categoryItem);
    }

    public interface AudioItemSelectedListener {
        void onItemSelected(String item);
    }

}
