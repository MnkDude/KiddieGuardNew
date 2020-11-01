// LoginActivity.java
package com.nizam.training.parentalcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText loginPin;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.login);
        loginPin = findViewById(R.id.loginPin);
        btnLogin = findViewById(R.id.btnLogIn);
    }

    public void onClick(View v) {
        try {
            SharedPreferences sf = getSharedPreferences("PinNumber", MODE_PRIVATE);
            SharedPreferences.Editor editor = sf.edit();
            editor.putInt("loginPin", Integer.parseInt(loginPin.getText().toString())).apply();
            int pin1 = sf.getInt("Pin", 0);
            int pin2 = sf.getInt("loginPin", 0);
            if (pin1 == pin2) {
                Intent intent = new Intent(getApplicationContext(), TaskList.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.pinwrongmsg), Toast.LENGTH_SHORT).show();
            }
            editor.putInt("loginPin", 0).apply();
        } catch (Throwable t) {
            Toast.makeText(LoginActivity.this, getString(R.string.pinwrongmsg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}