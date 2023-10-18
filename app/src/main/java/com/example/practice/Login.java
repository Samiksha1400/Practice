package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity
{
    EditText emailId,passWord;
    Button loginButton;
    ProgressBar progressBar;
    TextView createAccountText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailId = findViewById(R.id.emailId);
        passWord = findViewById(R.id.passWord);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        createAccountText = findViewById(R.id.createAccountText);

        //Lambda Function
        loginButton.setOnClickListener(view -> loginUser());
        createAccountText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Login.this,CreateAccount.class));
            }
        });
    }
    void loginUser()
    {
        String email = emailId.getText().toString();
        String password = passWord.getText().toString();

        boolean isValidated = validateData(email,password);
        if (!isValidated)
        {
            return;
        }

        loginAccountDB(email,password);
    }

    void loginAccountDB(String email,String password)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(Task<AuthResult> task)
            {
                changeInProgress(false);
                if (task.isSuccessful())
                {
                    //login is sucessful
                    if (firebaseAuth.getCurrentUser().isEmailVerified())
                    {
                        //login is successful and email is verified then go to Homepage
                        Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this,Homepage.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Email Not Verified,Please Verify Your Email", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    //login failed
                    Toast.makeText(Login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //Progressbar
    void changeInProgress(boolean inProgress)
    {
        if (inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    //Validate the data that are input by user.
    boolean validateData(String email,String password)
    {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailId.setError("Email is Invalid");
            return false;
        }
        if (password.length()<6)
        {
            passWord.setError("Password length is invalid");
            return false;
        }
        return true;
    }


}