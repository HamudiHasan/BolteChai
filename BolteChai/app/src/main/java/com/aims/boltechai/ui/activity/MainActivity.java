package com.aims.boltechai.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.aims.boltechai.R;
import com.aims.boltechai.model.Category;
import com.aims.boltechai.ui.fragment.ActivityListFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd;

    private ActivityListFragment activeFragment;

    String role;

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
       // setLanguage();
        if (isFirstTime()) {
            startActivity(new Intent(MainActivity.this, MasterLoginActivity.class));
        }

        fabAdd = (FloatingActionButton) findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeFragment.onAddButtonClicked();
            }
        });

        if (getRoleName().equals("Parent")) {
            if (fabAdd.getVisibility() == View.GONE) {
                fabAdd.setVisibility(View.VISIBLE);
            }
        } else {
            if (fabAdd.getVisibility() == View.VISIBLE) {
                fabAdd.setVisibility(View.GONE);
            }
        }

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, activeFragment
                , "main_fragment").commit();
    }

    private boolean isFirstTime() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = preferences.getString("BolteChaiFirstTime", "yes");
        if (name.equals("yes")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (getRoleName().equals("Parent")) {
            menu.getItem(3).setTitle(R.string.child_mode);
        } else {
            menu.getItem(3).setTitle(R.string.parent_mode);
        }
        return true;
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
        } else if (id == R.id.action_language) {

            final CharSequence[] items = { "Bangla", "English",
                    "Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.action_language);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Bangla")) {
                        setLocale("bn");
                        dialog.dismiss();
                    } else if (items[item].equals("English")) {
                        setLocale("en");
                        dialog.dismiss();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();


            return true;
        } else if (id == R.id.action_column) {
            showDialog();
            return true;
        } else if (id == R.id.action_user_mode) {

            if (getRoleName().equals("Parent")) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("BolteChaiRole", "Child");
                editor.commit();
                restartActivity();
            } else {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("BolteChaiRole", "Parent");
                editor.commit();
                restartActivity();
            }

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
        if (myFragment != null && myFragment.isVisible()) {
            activeFragment = myFragment;
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BolteChaiLanguage", lang);
        editor.commit();

        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_select_columns);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText etColumnsNumber = (EditText) dialog.findViewById(R.id.et_dialog_number_of_columns);
        Button popBtnCancel = (Button) dialog.findViewById(R.id.btn_cancel_dialoug_column);
        Button popBtnSave = (Button) dialog.findViewById(R.id.btn_save_dialoug_column);

        popBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        popBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidColumn(etColumnsNumber)) {
                    saveToSharePref("BolteChaiImage", Integer.parseInt(etColumnsNumber.getText().toString()));
                    dialog.dismiss();
                    restartActivity();
                } else {
                    Toast.makeText(MainActivity.this, R.string.column_error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });


        if (dialog != null)
            dialog.show();
    }

    private static boolean isValidColumn(EditText etText) {
        // check EditText empty or not
        if (etText.getText().toString().trim().length() > 0) {
            if (Integer.parseInt(etText.getText().toString()) > 0 && Integer.parseInt(etText.getText().toString()) <= 4) {
                return true;
            }
        }


        return false;
    }

    public void saveToSharePref(String name, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public String getRoleName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString("BolteChaiRole", "Parent");

    }

}
