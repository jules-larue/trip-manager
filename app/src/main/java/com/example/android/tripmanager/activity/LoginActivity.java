package com.example.android.tripmanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.UserConnectedManager;
import com.example.android.tripmanager.database.exception.UserNotFoundException;
import com.example.android.tripmanager.database.exception.WrongPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditUsername;

    private EditText mEditPassword;

    private TextView mTvSignUp;

    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditUsername = findViewById(R.id.edit_login_username);
        mEditPassword = findViewById(R.id.edit_login_password);

        setTitle(R.string.log_in);

        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEditUsername.getText().toString();
                String password = mEditPassword.getText().toString();
                
                if (username.isEmpty() || password.isEmpty()) {
                    // At least one field empty
                    Toast.makeText(LoginActivity.this, "Please check your credentials.", Toast.LENGTH_SHORT).show();
                }

                try {
                    UserConnectedManager.connectUser(getApplicationContext(), username, password);

                    // We succeed if we get here
                    Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                } catch (UserNotFoundException e) {
                    // Wrong username
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "This username does not exist.", Toast.LENGTH_LONG).show();
                } catch (WrongPasswordException e) {
                    // Username found, but wrong password
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "The password is incorrect.", Toast.LENGTH_LONG).show();
                }
            }
        });

        mTvSignUp = findViewById(R.id.tv_no_account_yet);
        mTvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}
