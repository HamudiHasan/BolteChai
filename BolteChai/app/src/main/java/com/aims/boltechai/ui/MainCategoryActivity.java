package com.aims.boltechai.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.aims.boltechai.R;
import com.aims.boltechai.model.Category;

import java.util.List;

public class MainCategoryActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd;

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


        fabAdd = (FloatingActionButton) findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use this to add new categories
                Toast.makeText(MainCategoryActivity.this, "This is my Fab !!", Toast.LENGTH_SHORT).show();

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

                List<Category> categories = new Select().from(Category.class).where("parentId = ?",1).execute();
                if (categories.size() > 0) {

                    for(int i=0; i<categories.size();i++){
                        Category category1 = categories.get(i);

                        Log.d("DB Result",category1.categoryTitle+"");
                    }

                }

                }
        });
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
