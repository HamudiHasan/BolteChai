package com.aims.boltechai.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.aims.boltechai.R;
import com.aims.boltechai.model.ActivityItem;
import com.aims.boltechai.model.Category;
import com.aims.boltechai.ui.adapter.CategoryAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainCategoryActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RECORD = 1;
    private static final int SELECT_IAMGE_FILE = 2;
    private FloatingActionButton fabAdd;

    RecyclerView recyclerView;
    CategoryAdapter adapter;
    private List<Category> categoriesItems = new ArrayList<Category>();
    private GridLayoutManager gridLayout;
    ViewFlipper viewFlipper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // configure db
        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Category.class);
        ActiveAndroid.initialize(config.create());

        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);

        recyclerView = (RecyclerView) findViewById(R.id.rv_main_category);
        gridLayout = new GridLayoutManager(MainCategoryActivity.this, 2);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        adapter = new CategoryAdapter(categoriesItems, this.getApplicationContext());
        recyclerView.setAdapter(adapter);
        addRecycleItems();

        fabAdd = (FloatingActionButton) findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use this to add new categories

                showDialog();
                // insert
               /* Category category = new Category(1,"Sonet","Hasan");
                category.save();*/

                // update
              /*  new Update(Category.class)
                        .set("categoryTitle = ? ","Takvir")
                        .where("categoryId = ?", 1)
                        .execute();*/

                //delete
             /*   new Delete().from(Category.class).where("categoryId = ?",1).execute();*/

                // Get All Data from DB
                /*List<Category> categories = new Select().from(Category.class).where("parentId = ?",1).execute();
                if (categories.size() > 0) {

                    for(int i=0; i<categories.size();i++){
                        Category category1 = categories.get(i);

                        Log.d("DB Result",category1.categoryTitle+"");
                    }

                }*/

            }
        });
    }
    public void addRecycleItems(){
        List<Category> categories = new Select().from(Category.class).execute();
        if (categories.size() > 0) {
            categoriesItems.clear();
            viewFlipper.setDisplayedChild(1);
            categoriesItems.addAll(categories);
            adapter.notifyDataSetChanged();
        }
    }
    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add_new_main_catagory);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText etCategoryTitle = (EditText) dialog.findViewById(R.id.et_category_title_new_category);

        final View audioLayout = dialog.findViewById(R.id.layout_audio_new_category);
        final TextView tvCategoryAudioTitle = (TextView) dialog.findViewById(R.id.tv_layout_audio_title_new_category);
        final ImageButton ibAudio = (ImageButton) dialog.findViewById(R.id.ib_layout_audio_new_category);

        final ImageView ivCategoryImage = (ImageView) dialog.findViewById(R.id.iv_category_image_new_category);

        Button btnSubmit = (Button) dialog.findViewById(R.id.btn_add_new_category);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel_new_category);

        ivCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_IAMGE_FILE);
            }
        });

        audioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Pick from file", "Record new Audio"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainCategoryActivity.this);
                builder.setCancelable(true);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Pick from file")) {

                        } else if (items[item].equals("Record new Audio")) {
                            Intent recordIntent = new Intent(
                                    MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                            startActivityForResult(recordIntent, REQUEST_CODE_RECORD);
                        }
                    }
                });
                builder.show();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category(1, etCategoryTitle.getText().toString(), "Hasan");
                category.save();
                addRecycleItems();
                dialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


            switch (requestCode) {
                case REQUEST_CODE_RECORD:
                    data.getDataString();
                    Toast.makeText(MainCategoryActivity.this, data.getDataString().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case SELECT_IAMGE_FILE:

                    Bitmap resultData = null;
                    try {
                        resultData = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
