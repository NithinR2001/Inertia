package com.dkinfo.inertia.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.dkinfo.inertia.CalanderActivity;
import com.dkinfo.inertia.R;
import com.google.android.material.textfield.TextInputLayout;

public class Sign_up extends AppCompatActivity {

    Button create;
    TextInputLayout s_username, s_number, s_password, s_repassword;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        create = findViewById(R.id.sign_up);
        s_username = findViewById(R.id.user);
        s_number = findViewById(R.id.number);
        s_password = findViewById(R.id.pass);
        s_repassword = findViewById(R.id.repass);

    }

    public void create(View view){
        String uName = s_username.getEditText().getText().toString().trim();
        String unum = s_number.getEditText().getText().toString().trim();
        String upass = s_password.getEditText().getText().toString().trim();
        String urepass = s_repassword.getEditText().getText().toString().trim();
        if (!uName.isEmpty()&& !unum.isEmpty() &&!upass.isEmpty() && !urepass.isEmpty()) {
            if (upass.equals(urepass)) {
                Toast.makeText(getBaseContext(), "Account created successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Sign_up.this, CalanderActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getBaseContext(), "Password and Re-password is mismatch", Toast.LENGTH_LONG);

            }
        }else {
            Toast.makeText(getBaseContext(), "Everything is required", Toast.LENGTH_LONG).show();
        }
    }

    public void login(View view){
        Intent intent = new Intent(Sign_up.this,Login.class);
        startActivity(intent);
    }
}
