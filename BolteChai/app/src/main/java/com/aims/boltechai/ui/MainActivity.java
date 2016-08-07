package com.aims.boltechai.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.aims.boltechai.R;
import com.aims.boltechai.model.Category;
import com.aims.boltechai.ui.fragment.ActivityListFragment;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd;

    private ActivityListFragment activeFragment;

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
        activeFragment = new ActivityListFragment();
        fabAdd = (FloatingActionButton) findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFragment.onAddButtonClicked();
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, activeFragment
                , "main_fragment").commit();
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

    public void moveToFragment(long id) {
        activeFragment = new ActivityListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ActivityListFragment.PARENT_ID, id);
        activeFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, activeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityListFragment myFragment = (ActivityListFragment) getSupportFragmentManager().findFragmentByTag("main_fragment");
        if(myFragment!=null && myFragment.isVisible()){
            activeFragment = myFragment;
        }
    }
}
