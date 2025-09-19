package com.alac.pocketexpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alac.pocketexpress.entity.Staff;
import com.alac.pocketexpress.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    EditText txtRegisterUsername, txtRegisterPassword, txtConfirmPassword;
    Button btnRegister;
    TextView btnSignInPage;
    String username, password, confirmPassword, registerDate;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        txtRegisterUsername = findViewById(R.id.txtRegisterUsername);
        txtRegisterPassword = findViewById(R.id.txtRegisterPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnSignInPage = findViewById(R.id.btnSignInPage);
        db = new DatabaseHelper(RegisterActivity.this);
        
        btnRegister.setOnClickListener(v ->{
            username = txtRegisterUsername.getText().toString();
            password = txtRegisterPassword.getText().toString();
            confirmPassword = txtConfirmPassword.getText().toString();
            SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            registerDate = formatter.format(date);

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();


            }else if(confirmPassword.equals(password)){

                Staff staff = new Staff(username, password, registerDate);
                db.createStaff(staff);

                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
                finish();

            }else{
                Toast.makeText(this, "PASSWORDS DON'T MATCH", Toast.LENGTH_SHORT).show();
            }
            
        });

        btnSignInPage.setOnClickListener(v ->{
            Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}