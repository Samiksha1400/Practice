package com.example.practice;

//import androidx.annotation.NonNull;
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

public class CreateAccount extends AppCompatActivity
{
    EditText emailId,passWord,confirmPassword;
    Button signupButton;
    ProgressBar progressBar;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        emailId = findViewById(R.id.emailId);
        passWord = findViewById(R.id.passWord);
        confirmPassword = findViewById(R.id.confirmPassword);
        signupButton = findViewById(R.id.signupButton);
        progressBar = findViewById(R.id.progressBar);
        loginText = findViewById(R.id.loginText);

        signupButton.setOnClickListener(view -> createAccount());
        loginText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(CreateAccount.this,Login.class));
            }
        });

    }

        void createAccount()
        {
            String email = emailId.getText().toString();
            String password = passWord.getText().toString();
            String confirmPass = confirmPassword.getText().toString();

            boolean isValidated = validateData(email,password,confirmPass);
            if (!isValidated)
            {
                return;
            }
            
            createAccountDB(email,password);

        }

        //Authentication in firebase
        void createAccountDB(String email,String password)
        {
            changeInProgress(true);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task)
                {
                    changeInProgress(false);
                    if (task.isSuccessful())
                    {
                        Toast.makeText(CreateAccount.this, "Account Created Sucessfully, Check email to Verify", Toast.LENGTH_SHORT).show();
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(CreateAccount.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            /*firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    changeInProgress(false);
                    if (task.isSuccessful())
                    {
                        Toast.makeText(CreateAccount.this, "Account Created Successfully, Check email to verify", Toast.LENGTH_SHORT).show();
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(CreateAccount.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        }

        //Progressbar
        void changeInProgress(boolean inProgress)
        {
            if (inProgress)
            {
                progressBar.setVisibility(View.VISIBLE);
                signupButton.setVisibility(View.GONE);
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                signupButton.setVisibility(View.VISIBLE);
            }
        }

        //Validate the data that are input by user.
        boolean validateData(String email,String password,String confirmPass)
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
            if (!password.equals(confirmPass))
            {
                confirmPassword.setError("Password Not matched");
                return false;
            }
            return true;
        }


}