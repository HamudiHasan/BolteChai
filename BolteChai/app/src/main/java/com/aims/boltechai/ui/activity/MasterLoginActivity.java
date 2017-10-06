package com.aims.boltechai.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aims.boltechai.R;
/**
 * Login Page for Future
 */
public class MasterLoginActivity extends AppCompatActivity {

    EditText etUserName;
    EditText etPassword;

    Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_login);

        etUserName = (EditText) findViewById(R.id.et_master_login_username);
        etPassword = (EditText) findViewById(R.id.et_master_login_password);

        btnLogIn = (Button) findViewById(R.id.btn_master_login);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(etUserName) && !isEmpty(etPassword)) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("BolteChaiUser", etUserName.getText().toString());
                    editor.putString("BolteChaiPass", etPassword.getText().toString());
                    editor.putString("BolteChaiFirstTime", "no");
                    editor.putString("BolteChaiRole", "Parent");
                    editor.commit();

                    startActivity(new Intent(MasterLoginActivity.this,MainActivity.class));

                }
            }
        });
    }

    private static boolean isEmpty(EditText etText) {
        // check EditText empty or not
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}
