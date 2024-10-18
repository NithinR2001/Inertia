package com.dkinfo.inertia.login;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.dkinfo.inertia.CalanderActivity;
import com.dkinfo.inertia.R;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    private boolean exitDialogShown = false;

    Button sign_in;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sign_in = findViewById(R.id.sign_in);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void sign_in(View view) {
        String uName = username.getEditText().getText().toString().trim();
        String pass = password.getEditText().getText().toString().trim();
        if(uName.equals("test")&&pass.equals("test"))
        {
            Intent i = new Intent(Login.this, CalanderActivity.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(getBaseContext(), "Login Failed",Toast.LENGTH_LONG).show();
        }

    }
    public void sign_up(View view) {
        Intent intent = new Intent(Login.this,Sign_up.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!exitDialogShown) {
                showExitDialog();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        exitApp();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        exitDialogShown = false;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                exitDialogShown = true;
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                exitDialogShown = false;
            }
        });
        dialog.show();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.parseColor("#2c653b"));
        negativeButton.setTextColor(Color.parseColor("#2c653b"));
    }

    private void exitApp() {
        finishAffinity(); // Close all activities
        System.exit(0); // Exit the app
    }
}