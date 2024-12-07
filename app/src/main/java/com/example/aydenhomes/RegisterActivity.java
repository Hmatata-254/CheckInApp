package com.example.aydenhomes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFirstNme , etLastNme , etPhone , etEmail ,etPass1, etPass2;
    private TextView btnCreateAc , btnToLogIn;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        
        //initialise views
        initViews();
        pd = new ProgressDialog(this);
        pd.setMessage("Creating account");

        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("user");




        //take the user to login activity when they press sign in
        btnToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //create user account (logic)
        btnCreateAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.show();

                String fNme = Objects.requireNonNull(etFirstNme.getText()).toString();
                String lastNme = Objects.requireNonNull(etLastNme.getText()).toString();
                String phone = Objects.requireNonNull(etPhone.getText()).toString();
                String email = Objects.requireNonNull(etEmail.getText()).toString();
                String pass1 = Objects.requireNonNull(etPass1.getText()).toString();
                String pass2 = Objects.requireNonNull(etPass2.getText()).toString();


                //check for errors(empty fields)
                if(TextUtils.isEmpty(fNme) || TextUtils.isEmpty(lastNme) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(pass1)){
                    pd.dismiss();
                    etFirstNme.setError("First Name required");
                    etLastNme.setError("Last name required");
                    etEmail.setError("Email required");
                    etPhone.setError("Mobile number required");
                    Toast.makeText(RegisterActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();

                }else if(!pass1.equals(pass2)){
                    pd.dismiss();
                    etPass1.setError("Passwords do not match");
                    etPass2.setError("Passwords do not match");

                }else{

                    registerUser(fNme,lastNme,phone,email,pass1,pass2);
                }
            }
        });
    }


    //register user and save data to my real time database
    private void registerUser(String fNme, String lastNme, String phone, String email, String pass1, String pass2) {


        auth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                User user = new User(fNme,lastNme,phone,email,pass1,null);

                userRef = FirebaseDatabase.getInstance().getReference("user");

                userRef.child(userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        //check if process was successful
                        if(task.isSuccessful()){
                            pd.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            intent.putExtra("firstName",user.getFirstName());
                            startActivity(intent);
                            finish();

                            Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //handle errors
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    public void initViews(){


        etFirstNme = findViewById(R.id.etFirstNme);
        etLastNme = findViewById(R.id.etLastNme);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPass1 = findViewById(R.id.etPass1);
        etPass2 = findViewById(R.id.etPass2);

        btnCreateAc = findViewById(R.id.btnCreateAc);
        btnToLogIn = findViewById(R.id.btnToLogIn);
    }
}