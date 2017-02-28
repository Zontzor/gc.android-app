package com.example.alex.glucosecoach.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.ApiManager;
import com.example.alex.glucosecoach.models.User;
import com.example.alex.glucosecoach.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 2/7/17.
 */

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText _usernameText;
    EditText _emailText;
    EditText _passwordText;
    EditText _reEnterPasswordText;
    Button _signupButton;
    TextView _loginLink;

    ApiManager apiManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        apiManager = new ApiManager();

        _usernameText = (EditText) findViewById(R.id.username);
        _emailText = (EditText) findViewById(R.id.input_username);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        //_signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        final User user = new User(username, email, password);

        UserService userService = apiManager.getUserService("");
        Call<String> call = userService.getUsername(user.getUsername());
        call.enqueue(new Callback<String >() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();

                if (!response.isSuccessful()) {
                    onSignupSuccess(user);
                } else {
                    _usernameText.setError("Username already in use");
                    onSignupFailed();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


    public void onSignupSuccess(User user) {
        UserService userService = apiManager.getUserService("");
        Call<User> call = userService.postUser(user);
        call.enqueue(new Callback<User >() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    goToSignin();
                } else {
                    setResult(RESULT_CANCELED, null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void goToSignin() {
        _signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Signup success", Toast.LENGTH_LONG).show();
        Intent mainActivity = new Intent(this, LoginActivity.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivity);
        finish();
    }
}
