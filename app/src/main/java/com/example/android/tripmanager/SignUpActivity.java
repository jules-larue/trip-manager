package com.example.android.tripmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.UserDao;
import com.example.android.tripmanager.database.exception.NicknameAlreadyExistsException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEditUsername;
    private EditText mEditFirstName;
    private EditText mEditLastName;
    private EditText mEditBirthDate;
    private EditText mEditPassword;
    private EditText mEditPasswordConfirmation;
    private Button mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEditUsername = findViewById(R.id.edit_sign_up_username);
        mEditFirstName = findViewById(R.id.edit_sign_up_first_name);
        mEditLastName = findViewById(R.id.edit_sign_up_last_name);
        mEditBirthDate = findViewById(R.id.edit_sign_up_birth_date);
        mEditPassword = findViewById(R.id.edit_sign_up_password);
        mEditPasswordConfirmation = findViewById(R.id.edit_sign_up_password_confirmation);
        
        mBtnSignUp = findViewById(R.id.btn_sign_up);
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFormValidator signUpValidator = new SignUpFormValidator(SignUpActivity.this);
                try {
                    if (!signUpValidator.validate()) {
                        // At least one incorrect field
                        Toast.makeText(SignUpActivity.this, "Please that all the fields are correct.", Toast.LENGTH_SHORT).show();
                    } else {
                        // All fields are OK,
                        // we can add the user to the database
                        DateFormat birthDateFormat = new SimpleDateFormat(UserBean.BIRTH_DATE_FORMAT);
                        Date birthDate = birthDateFormat.parse(mEditBirthDate.getText().toString());
                        UserBean newUser = new UserBean(mEditUsername.getText().toString(),
                                mEditPassword.getText().toString(),
                                mEditFirstName.getText().toString(),
                                mEditLastName.getText().toString(),
                                birthDate);
                        UserDao userDao = new UserDao(SignUpActivity.this);

                        // Insert the new user
                        userDao.insertUser(newUser);

                        // Here insertion is successful,
                        // so we go back to log in screen
                        Toast.makeText(SignUpActivity.this, "You have been successfully signed up!", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                } catch (NicknameAlreadyExistsException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this, "This username has already been taken.", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class SignUpFormValidator {

        private SignUpActivity mSignUpActivity;

        public final DateFormat mBirthDateFormat = new SimpleDateFormat("mm/dd/yyyy");

        public SignUpFormValidator(SignUpActivity signUpActivity) {
            mSignUpActivity = signUpActivity;
        }

        /**
         * Checks that all the fields of the sign up form are correct.
         * @return true if all the fields are correct; false otherwise
         */
        public boolean validate() {

            // Get all inputs values
            String username = mSignUpActivity.mEditUsername.getText().toString();
            String firstName = mSignUpActivity.mEditFirstName.getText().toString();
            String lastName = mSignUpActivity.mEditLastName.getText().toString();
            String birthDateString = mSignUpActivity.mEditBirthDate.getText().toString();
            String password = mSignUpActivity.mEditPassword.getText().toString();
            String passwordConfirmation = mEditPasswordConfirmation.getText().toString();

            // Check birth date format
            Date birthDate = null;
            try {
                birthDate = mBirthDateFormat.parse(birthDateString);

                Date now = new Date();
                if (birthDate.after(now)) {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }

            // Check if fields are empty
            if (username.isEmpty()) {
                return false;
            }

            if (firstName.isEmpty()) {
                return false;
            }

            if (lastName.isEmpty()) {
                return false;
            }

            if (password.isEmpty()) {
                return false;
            }

            if (passwordConfirmation.isEmpty()) {
                return false;
            }

            if (!password.equals(passwordConfirmation)) {
                return false;
            }

            return true;
        }
    }
}
